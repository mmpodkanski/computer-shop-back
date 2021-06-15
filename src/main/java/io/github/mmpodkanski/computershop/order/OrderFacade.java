package io.github.mmpodkanski.computershop.order;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.mmpodkanski.computershop.cart.CartFacade;
import io.github.mmpodkanski.computershop.cart.CartItemQueryRepository;
import io.github.mmpodkanski.computershop.cart.dto.CartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.order.dto.OrderDto;
import io.github.mmpodkanski.computershop.order.dto.OrderItemDto;
import io.github.mmpodkanski.computershop.order.enums.EOrderStatus;
import io.github.mmpodkanski.computershop.product.ProductFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
class OrderFacade {
    private final OrderRepository repository;
    private final OrderQueryRepository queryRepository;
    private final CartItemQueryRepository cartItemQueryRepository;
    private final OrderFactory orderFactory;
    private final ProductFacade productFacade;
    private final CartFacade facade;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    OrderFacade(
            final OrderRepository repository,
            final OrderQueryRepository queryRepository,
            final CartItemQueryRepository cartItemQueryRepository,
            final OrderFactory orderFactory,
            final ProductFacade productFacade,
            final CartFacade facade
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.cartItemQueryRepository = cartItemQueryRepository;
        this.orderFactory = orderFactory;
        this.productFacade = productFacade;
        this.facade = facade;
    }

    OrderDto placeOrder(Customer customer) {
        if (customer.getDetails() == null) {
            throw new ApiBadRequestException("Firstly update customer details please!");
        }
        List<CartItemDto> cartList = cartItemQueryRepository.findAllByCustomerOrderByCreatedAtDesc(customer);

        if (cartList.size() == 0) {
            throw new ApiBadRequestException("No products in cart, please add some!");
        }

        var cart =
                new CartDto(
                        cartList,
                        cartList.stream().map(itemDto -> {
                            BigDecimal price = itemDto.getProduct().getPrice();
                            BigDecimal quantity = BigDecimal.valueOf(itemDto.getQuantity());
                            return price.multiply(quantity);
                        }).reduce(BigDecimal.ZERO, BigDecimal::add));

        var orderItems = cart.getCarts().stream().map(item -> {
            var orderItemDto = OrderItemDto.create(0, item.getQuantity(), item.getProduct().getPrice(), item.getProduct());
            return orderFactory.toNewEntity(orderItemDto);
        }).collect(Collectors.toSet());

        facade.deleteAllCartItems(customer);
        var order = repository.save(new Order(
                cart.getTotalCost(),
                orderItems,
                customer,
                EOrderStatus.PENDING
        ));

        return orderFactory.toDto(order);
    }

    @Transactional
    public void cancelOrder(Customer customer, int id) {
        var order = queryRepository.findDtoByIdAndCustomer(id, customer)
                .orElseThrow(() -> new ApiNotFoundException("Order with that id not exists or is not yours"));

        var status = order.getStatus();
        if (!status.equals(EOrderStatus.PENDING.toString())) {
            throw new ApiNotFoundException("You cant cancel order, status is: " + status);
        }

        repository.findById(id).ifPresent(Order::cancelOrder);
    }

    @Transactional
    public Session createSession(int orderId) throws StripeException {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new ApiNotFoundException("Order not found!"));

        if (!order.getStatus().equals(EOrderStatus.PENDING)) {
            throw new ApiBadRequestException("Order status is: " + order.getStatus().toString());
        }
        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        String successURL = baseURL + "/payment/success?orderId=" + orderId;
        String failedURL = baseURL + "/payment/failed?orderId="+ orderId;
        Stripe.apiKey = apiKey;

        order.getItems().forEach(e -> sessionItemsList.add(createSessionLineItem(e)));
        var session = Session.create(SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .setClientReferenceId(String.valueOf(order.getCustomer().getId()))
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build());

        order.setPaymentId(session.getId());

        return session;
    }

    @Transactional
    public void checkSession(int orderId) throws StripeException {
        Stripe.apiKey = apiKey;
        var order = repository.findById(orderId)
                .orElseThrow(() -> new ApiNotFoundException("Order with that id not exists: " + orderId));

        var session = Session.retrieve(order.getPaymentId());
        var paymentStatus = session.getPaymentStatus();

        switch (paymentStatus) {
            case ("unpaid"):
                order.setStatus(EOrderStatus.FAILED);
                break;
            case ("paid"):
                order.setStatus(EOrderStatus.PAID);
                var items = order.getItems();
                items.forEach(item -> productFacade.decreaseProductStock(item.getProduct().getId(), item.getQuantity()));
                break;
        }
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("pln")
                .setUnitAmount((item.getPrice().longValue()) * 100)
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProduct().getName())
                                .build())
                .build();
    }

    private SessionCreateParams.LineItem createSessionLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(item))
                .setQuantity(Long.parseLong(String.valueOf(item.getQuantity())))
                .build();
    }



}

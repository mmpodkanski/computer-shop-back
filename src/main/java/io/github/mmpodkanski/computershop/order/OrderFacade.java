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
import io.github.mmpodkanski.computershop.customer.CustomerFactory;
import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.order.dto.OrderDto;
import io.github.mmpodkanski.computershop.order.dto.OrderItemDto;
import io.github.mmpodkanski.computershop.order.enums.EOrderStatus;
import io.github.mmpodkanski.computershop.product.ProductFacade;
import io.github.mmpodkanski.computershop.product.ProductFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class OrderFacade {
    private final OrderRepository repository;
    private final OrderQueryRepository queryRepository;
    private final CartItemQueryRepository cartItemQueryRepository;
    private final CustomerFactory customerFactory;
    private final CartFacade facade;
    private final ProductFactory productFactory;
    private final ProductFacade productFacade;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    OrderFacade(
            final OrderRepository repository,
            final OrderQueryRepository queryRepository,
            final CartItemQueryRepository cartItemQueryRepository,
            final CustomerFactory customerFactory,
            final CartFacade facade,
            final ProductFactory productFactory,
            final ProductFacade productFacade
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.cartItemQueryRepository = cartItemQueryRepository;
        this.customerFactory = customerFactory;
        this.facade = facade;
        this.productFactory = productFactory;
        this.productFacade = productFacade;
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
                            double price = itemDto.getProduct().getPrice();
                            return price * itemDto.getQuantity();
                        }).reduce(0.0, Double::sum)
                );

        var orderItems = cart.getCarts().stream().map(item -> new OrderItem(
                item.getQuantity(),
                productFactory.toEntity(item.getProduct()),
                item.getProduct().getPrice()
        )).collect(Collectors.toSet());

        facade.deleteCartItems(customer);
        var order = repository.save(new Order(
                cart.getTotalCost(),
                orderItems,
                customer,
                EOrderStatus.PENDING
        ));
//        orderItems.forEach(item -> productFacade.decreaseProductStock(item.getProduct().getId(), item.getQuantity()));

        return Dto(order);
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
//        order.getItems().forEach(item -> productFacade.increaseProductStock(item.getProduct().getId(), item.getQuantity()));
    }

    Session createSession(int orderId) throws StripeException {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new ApiNotFoundException("Order not found!"));

        if (!order.getStatus().equals(EOrderStatus.PENDING)) {
            throw new ApiBadRequestException("Orders status is: " + order.getStatus().toString());
        }
        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failed";
        Stripe.apiKey = apiKey;

        order.getItems().forEach(e -> sessionItemsList.add(createSessionLineItem(e)));
        return Session.create(SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build());
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("pln")
                .setUnitAmount(((long) item.getPrice()) * 100)
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

    private OrderDto Dto(Order entity) {
        return OrderDto.create(
                entity.getId(),
                entity.getTotalCost(),
                entity.getItems().stream().map(this::Dto).collect(Collectors.toSet()),
                customerFactory.toDto(entity.getCustomer()),
                entity.getStatus().toString()
        );
    }

    private OrderItemDto Dto(OrderItem entity) {
        return OrderItemDto.create(
                entity.getId(),
                entity.getQuantity(),
                entity.getPrice(),
                productFactory.toDto(entity.getProduct())
        );
    }
}

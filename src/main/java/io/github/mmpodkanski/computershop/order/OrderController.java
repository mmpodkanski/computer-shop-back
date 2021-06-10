package io.github.mmpodkanski.computershop.order;

import com.stripe.exception.StripeException;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.order.dto.OrderDto;
import io.github.mmpodkanski.computershop.order.dto.checkout.StripeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderFacade facade;
    private final OrderQueryRepository queryRepository;

    OrderController(final OrderFacade facade, final OrderQueryRepository queryRepository) {
        this.facade = facade;
        this.queryRepository = queryRepository;
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderDto> readSomeOrder(
            @AuthenticationPrincipal Customer customer,
            @PathVariable int id
    ) {
        var result = queryRepository.findDtoByIdAndCustomer(id, customer)
                .orElseThrow(() -> new ApiNotFoundException("Order with that id not exists or is not yours"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<OrderDto>> readAllCustomerOrders(
            @AuthenticationPrincipal Customer customer
    ) {
        var result = queryRepository.findAllDtoByCustomer(customer);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal Customer customer) {
        var result = facade.placeOrder(customer);
        logger.info("Customer(id): " + customer.getId() + " created a new order!");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(value = "/checkout", params = "orderId")
    ResponseEntity<StripeDto> createCheckout(@RequestParam int orderId) throws StripeException {
        var result = facade.createSession(orderId);
        var response = new StripeDto(result.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/check-session", params = "orderId")
    ResponseEntity<Void> checkSession(@RequestParam int orderId) throws StripeException {
        facade.checkSession(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal Customer customer,
            @PathVariable int id
    ) {
        facade.cancelOrder(customer, id);
        logger.info("Customer(id): " + customer.getId() + " canceled order!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

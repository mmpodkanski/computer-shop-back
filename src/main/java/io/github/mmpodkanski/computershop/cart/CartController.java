package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cart")
class CartController {
    private final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartItemQueryRepository queryRepository;
    private final CartFacade facade;


    CartController(final CartItemQueryRepository queryRepository, final CartFacade facade) {
        this.queryRepository = queryRepository;
        this.facade = facade;
    }

    @GetMapping
    ResponseEntity<CartDto> readAllCart(@AuthenticationPrincipal Customer customer) {
        List<CartItemDto> cartList = queryRepository.findAllByCustomerIdOrderByCreatedAtDesc(customer.getId());

        var result = new CartDto(cartList, cartList.stream().map(cart -> {
            double price = cart.getProduct().getPrice();
            return price * cart.getQuantity();
        }).reduce(0.0, Double::sum));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<CartItemDto> createItemCart(
            @Valid @RequestBody AddToCartDto addCartDto,
            @AuthenticationPrincipal Customer customer
    ) {
        var result = facade.addToCart(addCartDto, customer.getId());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> updateCartItem(
            @Valid @RequestBody AddToCartDto addToCartDto,
            @AuthenticationPrincipal Customer customer,
            @PathVariable int id
    ) {
        facade.updateCartItem(addToCartDto, id, customer.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteItemCart(
            @AuthenticationPrincipal Customer customer,
            @PathVariable int id
    ) {
        facade.deleteCartItem(id, customer.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    ResponseEntity<Void> deleteAllCarts(
            @AuthenticationPrincipal Customer customer
    ) {
        facade.deleteCartItems(customer.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

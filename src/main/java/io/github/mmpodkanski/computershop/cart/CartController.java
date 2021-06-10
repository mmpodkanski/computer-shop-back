package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cart")
class CartController {
    private final CartItemQueryRepository queryRepository;
    private final CartFacade facade;


    CartController(final CartItemQueryRepository queryRepository, final CartFacade facade) {
        this.queryRepository = queryRepository;
        this.facade = facade;
    }

    @GetMapping
    ResponseEntity<CartDto> readAllCart(@AuthenticationPrincipal Customer customer) {
        List<CartItemDto> cartList = queryRepository.findAllByCustomerOrderByCreatedAtDesc(customer);

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
        var result = facade.addToCart(addCartDto, customer);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteItemCart(
            @AuthenticationPrincipal Customer customer,
            @PathVariable int id
    ) {
        facade.deleteCartItem(id, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    ResponseEntity<Void> deleteAllCarts(
            @AuthenticationPrincipal Customer customer
    ) {
        facade.deleteAllCartItems(customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

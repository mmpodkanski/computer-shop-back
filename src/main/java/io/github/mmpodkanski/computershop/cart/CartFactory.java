package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.customer.CustomerFactory;
import io.github.mmpodkanski.computershop.product.ProductFactory;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import org.springframework.stereotype.Service;

@Service
class CartFactory {
    private final CustomerFactory customerFactory;
    private final ProductFactory productFactory;

    CartFactory(final CustomerFactory customerFactory, final ProductFactory productFactory) {
        this.customerFactory = customerFactory;
        this.productFactory = productFactory;
    }

    CartItemDto toDto(CartItem entity) {
        return CartItemDto.create(
                entity.getId(),
                customerFactory.toDto(entity.getCustomer()),
                productFactory.toDto(entity.getProduct()),
                entity.getQuantity()
        );
    }

    CartItem toEntity(AddToCartDto addToCartDto, Customer customer, ProductDto productDto) {
        return new CartItem(productFactory.toEntity(productDto), addToCartDto.getQuantity(), customer);
    }
}

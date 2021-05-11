package io.github.mmpodkanski.computershop.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.mmpodkanski.computershop.customer.dto.CustomerDto;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;

import java.util.Calendar;

public interface CartItemDto {
    static CartItemDto create(
            final int id,
            final CustomerDto customerDto,
            final ProductDto productDto,
            final int quantity

    ) {
        return new CartItemDto.CartItemDtoImpl(id, customerDto, productDto, quantity);
    }

    int getId();

    CustomerDto getCustomer();

    ProductDto getProduct();

    int getQuantity();

    @JsonIgnore
    Calendar getCreatedAt();

    class CartItemDtoImpl implements CartItemDto {
        private final int id;
        private final CustomerDto customerDto;
        private final ProductDto productDto;
        private final int quantity;

        public CartItemDtoImpl(final int id, final CustomerDto customerDto, final ProductDto productDto, final int quantity) {
            this.id = id;
            this.customerDto = customerDto;
            this.productDto = productDto;
            this.quantity = quantity;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public CustomerDto getCustomer() {
            return customerDto;
        }

        @Override
        public ProductDto getProduct() {
            return productDto;
        }

        @Override
        public int getQuantity() {
            return quantity;
        }

        @Override
        public Calendar getCreatedAt() {
            return null;
        }
    }
}

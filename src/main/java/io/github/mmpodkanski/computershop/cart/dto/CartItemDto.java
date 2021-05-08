package io.github.mmpodkanski.computershop.cart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;

import java.util.Calendar;

public interface CartItemDto {
    static CartItemDto create(
            final int id,
            final int customerId,
            final ProductDto productDto,
            final int quantity

    ) {
        return new CartItemDto.CartItemDtoImpl(id, customerId, productDto, quantity);
    }

    int getId();

    int getCustomerId();

    ProductDto getProduct();

    int getQuantity();

    @JsonIgnore
    Calendar getCreatedAt();

    class CartItemDtoImpl implements CartItemDto {
        private final int id;
        private final int customerId;
        private final ProductDto productDto;
        private final int quantity;

        public CartItemDtoImpl(final int id, final int customerId, final ProductDto productDto, final int quantity) {
            this.id = id;
            this.customerId = customerId;
            this.productDto = productDto;
            this.quantity = quantity;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getCustomerId() {
            return customerId;
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

package io.github.mmpodkanski.computershop.order.dto;

import io.github.mmpodkanski.computershop.product.dto.ProductDto;

import java.math.BigDecimal;

public interface OrderItemDto {
    static OrderItemDto create(
            int id,
            int quantity,
            BigDecimal price,
            ProductDto product
    ) {
        return new OrderItemDtoImpl(id, quantity, price, product);
    }

    int getId();

    int getQuantity();

    BigDecimal getPrice();

    ProductDto getProduct();

    class OrderItemDtoImpl implements OrderItemDto {
        private final int id;
        private final int quantity;
        private final BigDecimal price;
        private final ProductDto product;

        OrderItemDtoImpl(final int id, final int quantity, final BigDecimal price, final ProductDto product) {
            this.id = id;
            this.quantity = quantity;
            this.price = price;
            this.product = product;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getQuantity() {
            return quantity;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public ProductDto getProduct() {
            return product;
        }
    }
}
package io.github.mmpodkanski.computershop.product.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;


@JsonDeserialize(as = ProductDto.ProductDtoImpl.class)
public interface ProductDto {
    static ProductDto create(
            final int id,
            final String name,
            final String description,
            final String code,
            final String category,
            final BigDecimal price,
            final String condition,
            final int quantity,
            final String imgLogoUrl

    ) {
        return new ProductDtoImpl(id, name, description, code, category, price, condition, quantity, imgLogoUrl);
    }


    int getId();

    String getName();

    String getDescription();

    String getCode();

    String getCategory();

    BigDecimal getPrice();

    String getCondition();

    int getQuantity();

    String getImgLogoUrl();


    class ProductDtoImpl implements ProductDto {
        private final int id;
        private final String name;
        private final String description;
        private final String code;
        private final String category;
        private final BigDecimal price;
        private final String condition;
        private final int quantity;
        private final String imgLogoUrl;


        ProductDtoImpl(
                final int id,
                final String name,
                final String description,
                final String code,
                final String category,
                final BigDecimal price,
                final String condition,
                final int quantity,
                final String imgLogoUrl
        ) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.code = code;
            this.category = category;
            this.price = price;
            this.condition = condition;
            this.quantity = quantity;
            this.imgLogoUrl = imgLogoUrl;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public String getCondition() {
            return condition;
        }

        @Override
        public int getQuantity() {
            return quantity;
        }

        @Override
        public String getImgLogoUrl() {
            return imgLogoUrl;
        }
    }
}

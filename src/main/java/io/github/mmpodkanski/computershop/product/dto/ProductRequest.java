package io.github.mmpodkanski.computershop.product.dto;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductRequest {
    @NotBlank(message = "Name of product can not be blank!")
    private final String name;
    @NotBlank(message = "Description can not be blank!")
    private final String description;
    private final String code;
    @NotNull(message = "Please choose category!")
    private final String category;
    @NotNull(message = "Please type price of product!")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private final double price;
    private final String condition;
    private final int quantity;
    private final String imgLogoUrl;

    ProductRequest(
            @NotBlank final String name,
            @NotBlank final String description,
            final String code,
            @NotBlank final String category,
            @NotNull final double price,
            final String condition,
            final int quantity,
            final String imgLogoUrl
    ) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.category = category;
        this.price = price;
        this.condition = condition;
        this.quantity = quantity;
        this.imgLogoUrl = imgLogoUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImgLogoUrl() {
        return imgLogoUrl;
    }
}

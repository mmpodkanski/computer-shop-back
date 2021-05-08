package io.github.mmpodkanski.computershop.cart.dto;

import javax.validation.constraints.NotNull;

public class AddToCartDto {
    private int id;
    @NotNull
    private int productId;
    @NotNull
    private int quantity;

    public AddToCartDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    void setProductId(final int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
}

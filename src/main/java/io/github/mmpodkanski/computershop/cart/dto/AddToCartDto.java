package io.github.mmpodkanski.computershop.cart.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AddToCartDto {
    private int id;
    @NotNull
    private int productId;
    @NotNull
    private int quantity;

    public AddToCartDto() {
    }

    public AddToCartDto(final int id, final int productId, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddToCartDto that = (AddToCartDto) o;
        return id == that.id && productId == that.productId && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, quantity);
    }
}

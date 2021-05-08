package io.github.mmpodkanski.computershop.cart.dto;

import java.util.List;

public class CartDto {
    private final List<CartItemDto> carts;
    private final double totalCost;

    public CartDto(final List<CartItemDto> carts, final double totalCost) {
        this.carts = carts;
        this.totalCost = totalCost;
    }

    public List<CartItemDto> getCarts() {
        return carts;
    }

    public  double getTotalCost() {
        return totalCost;
    }
}

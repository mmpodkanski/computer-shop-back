package io.github.mmpodkanski.computershop.cart.dto;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDto cartDto = (CartDto) o;
        return Double.compare(cartDto.totalCost, totalCost) == 0 && Objects.equals(carts, cartDto.carts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carts, totalCost);
    }
}

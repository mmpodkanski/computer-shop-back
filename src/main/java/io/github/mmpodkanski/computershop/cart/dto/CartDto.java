package io.github.mmpodkanski.computershop.cart.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CartDto {
    private final List<CartItemDto> carts;
    private final BigDecimal totalCost;

    public CartDto(final List<CartItemDto> carts, final BigDecimal totalCost) {
        this.carts = carts;
        this.totalCost = totalCost;
    }

    public List<CartItemDto> getCarts() {
        return carts;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDto cartDto = (CartDto) o;
        return Objects.equals(carts, cartDto.carts) && Objects.equals(totalCost, cartDto.totalCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carts, totalCost);
    }
}

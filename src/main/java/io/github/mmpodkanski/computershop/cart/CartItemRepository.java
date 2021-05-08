package io.github.mmpodkanski.computershop.cart;

import org.springframework.data.repository.Repository;

interface CartItemRepository extends Repository<CartItem, Integer> {
    CartItem findByIdAndCustomerId(int id, int userId); //*

    CartItem save(CartItem entity);

    void deleteByIdAndCustomerId(int id, int userId);

    void deleteAllByCustomerId(int id);
}

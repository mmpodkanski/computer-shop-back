package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.customer.Customer;
import org.springframework.data.repository.Repository;

interface CartItemRepository extends Repository<CartItem, Integer> {
    CartItem findByIdAndCustomer(int id, Customer customer); //*

    CartItem save(CartItem entity);

    void deleteByIdAndCustomer(int id, Customer customer);

    void deleteAllByCustomer(Customer customer);
}

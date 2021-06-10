package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.customer.Customer;
import org.springframework.data.repository.Repository;
import java.util.Optional;

interface CartItemRepository extends Repository<CartItem, Integer> {
    Optional<CartItem> findById(int id);

    CartItem save(CartItem entity);

    void deleteByIdAndCustomer(int id, Customer customer);

    void deleteAllByCustomer(Customer customer);
}

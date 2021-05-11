package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CartItemQueryRepository extends Repository<CartItem, Integer> {
    boolean existsById(int id);

    List<CartItemDto> findAllByCustomerOrderByCreatedAtDesc(Customer customer);

    CartItem findByIdAndCustomer(int cartId, Customer customer);
}

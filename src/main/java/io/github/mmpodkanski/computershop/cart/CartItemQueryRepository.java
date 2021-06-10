package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.product.Product;
import org.springframework.data.repository.Repository;
import java.util.Optional;

import java.util.List;

public interface CartItemQueryRepository extends Repository<CartItem, Integer> {
    boolean existsById(int id);

    List<CartItemDto> findAllByCustomerOrderByCreatedAtDesc(Customer customer);

    Optional<CartItem> findByIdAndCustomer(int cartId, Customer customer);

    Optional<CartItem> findByProductAndCustomer(Product productId, Customer customer);
}

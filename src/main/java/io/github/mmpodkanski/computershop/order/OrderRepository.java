package io.github.mmpodkanski.computershop.order;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface OrderRepository extends Repository<Order, Integer> {
    Optional<Order> findById(int id);

    Order save(Order entity);
}

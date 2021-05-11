package io.github.mmpodkanski.computershop.customer;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface CustomerRepository extends Repository<Customer, Integer> {
    Optional<Customer> findById(int id);

    void save(Customer entity);

    void delete(Customer entity);
}

package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.dto.CustomerDto;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CustomerQueryRepository extends Repository<Customer, Integer> {
    boolean existsById(int id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<CustomerDto> findAllBy();

    Optional<Customer> findByUsername(String username);

    Optional<CustomerDto> findDtoByUsername(String username);
}

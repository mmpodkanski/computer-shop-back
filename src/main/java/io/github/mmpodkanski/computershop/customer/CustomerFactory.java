package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.dto.CustomerDetailsDto;
import io.github.mmpodkanski.computershop.customer.dto.CustomerDto;
import io.github.mmpodkanski.computershop.customer.enums.EGender;
import org.springframework.stereotype.Service;

@Service
public class CustomerFactory {
    public CustomerDto toDto(Customer entity) {
        return CustomerDto.create(
          entity.getId(),
          entity.getUsername(),
          entity.getEmail(),
          toDto(entity.getDetails())
        );
    }

    public CustomerDetailsDto toDto(CustomerDetails customer) {
        return CustomerDetailsDto.create(
                customer.getId(),
                customer.getGender().toString(),
                customer.getAddressLine1(),
                customer.getAddressLine2(),
                customer.getPhone(),
                customer.getCity(),
                customer.getCountry()
        );
    }

    public CustomerDetails toEntity(CustomerDetailsDto dto, Customer customer) {
        return new CustomerDetails(
                EGender.valueOf(dto.getGender()),
                dto.getAddressLine1(),
                dto.getAddressLine2(),
                dto.getPhone(),
                dto.getCity(),
                dto.getCountry()
        );
    }
}

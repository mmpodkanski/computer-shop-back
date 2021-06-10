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
          entity.getDetails() == null ? null : toDto(entity.getDetails())
        );
    }

    CustomerDetailsDto toDto(CustomerDetails entity) {
        return CustomerDetailsDto.create(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender().toString(),
                entity.getAddressLine1(),
                entity.getAddressLine2(),
                entity.getPhone(),
                entity.getCity(),
                entity.getCountry()
        );
    }

    CustomerDetails toEntity(CustomerDetailsDto dto) {
        return new CustomerDetails(
                dto.getFirstName(),
                dto.getLastName(),
                EGender.valueOf(dto.getGender()),
                dto.getAddressLine1(),
                dto.getAddressLine2(),
                dto.getPhone(),
                dto.getCity(),
                dto.getCountry()
        );
    }
}

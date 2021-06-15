package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.auth.JwtUtils;
import io.github.mmpodkanski.computershop.customer.dto.CustomerDetailsDto;
import io.github.mmpodkanski.computershop.customer.dto.JwtResponse;
import io.github.mmpodkanski.computershop.customer.dto.LoginRequest;
import io.github.mmpodkanski.computershop.customer.dto.RegisterRequest;
import io.github.mmpodkanski.computershop.customer.enums.ERole;
import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class CustomerFacade {
    private final CustomerRepository repository;
    private final CustomerQueryRepository queryRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final CustomerFactory factory;

    CustomerFacade(
            @Qualifier("customerRepository") final CustomerRepository repository,
            final CustomerQueryRepository queryRepository,
            final AuthenticationManager authenticationManager,
            final PasswordEncoder encoder,
            final JwtUtils jwtUtils,
            final CustomerFactory factory
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.factory = factory;
    }

    void register(final RegisterRequest signUpRequest) {
        if (queryRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ApiBadRequestException("Username is already taken!");
        }
        if (queryRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ApiBadRequestException("Email is already in use!");
        }

        var user = new Customer(
                signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                ERole.ROLE_USER
        );

        repository.save(user);

    }

    JwtResponse login(final LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtAccessToken = jwtUtils.generateJwtToken(auth);

        var user = (Customer) auth.getPrincipal();
        String role = auth.getAuthorities().toString();

        return new JwtResponse(
                user.getId(),
                jwtAccessToken,
                user.getEmail(),
                user.getUsername(),
                role);
    }

    // TODO: change update method, "SET" variable is a bad idea
    CustomerDetailsDto addDetails(CustomerDetailsDto dto, Customer customer) {
        var details = factory.toEntity(dto);

        if (customer.getDetails() != null) {
            details.setId(customer.getDetails().getId());
        }

        customer.addInfo(details);
        repository.save(customer);
        return factory.toDto(details);
    }

}


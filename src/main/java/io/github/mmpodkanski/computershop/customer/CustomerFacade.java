package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.auth.JwtUtils;
import io.github.mmpodkanski.computershop.customer.dto.CustomerDetailsDto;
import io.github.mmpodkanski.computershop.customer.dto.JwtResponse;
import io.github.mmpodkanski.computershop.customer.dto.LoginRequest;
import io.github.mmpodkanski.computershop.customer.dto.RegisterRequest;
import io.github.mmpodkanski.computershop.customer.enums.EGender;
import io.github.mmpodkanski.computershop.customer.enums.ERole;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomerFacade {
    private final CustomerRepository repository;
    private final CustomerQueryRepository queryRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    CustomerFacade(
            final CustomerRepository repository,
            final CustomerQueryRepository queryRepository,
            final AuthenticationManager authenticationManager,
            final PasswordEncoder encoder,
            final JwtUtils jwtUtils
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    void signup(final RegisterRequest signUpRequest) {
        if (queryRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalStateException("Username is already taken!");
        }
        if (queryRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalStateException("Email is already in use!");
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

    @Transactional
    void addDetails(CustomerDetailsDto dto, Customer customer) {
        var details = new CustomerDetails(
                EGender.valueOf(dto.getGender()),
                dto.getAddressLine1(),
                dto.getAddressLine2(),
                dto.getPhone(),
                dto.getCity(),
                dto.getCountry()
        );
        customer.addInfo(details);
    }
}


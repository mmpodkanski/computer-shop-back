package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.dto.JwtResponse;
import io.github.mmpodkanski.computershop.customer.dto.LoginRequest;
import io.github.mmpodkanski.computershop.customer.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
class CustomerController {
    private final CustomerFacade service;

    CustomerController(final CustomerFacade service) {
        this.service = service;
    }

    @PostMapping("/login")
    ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = service.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        service.signup(signUpRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

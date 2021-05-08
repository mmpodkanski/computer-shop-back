package io.github.mmpodkanski.computershop.customer.auth;

import io.github.mmpodkanski.computershop.customer.CustomerQueryRepository;
import io.github.mmpodkanski.computershop.customer.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AuthService implements UserDetailsService {
    private final CustomerRepository repository;
    private final CustomerQueryRepository queryRepository;

    AuthService(final CustomerRepository repository, final CustomerQueryRepository queryRepository) {
        this.repository = repository;
        this.queryRepository = queryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
        var userDto = queryRepository.findByUsername(s)
                .orElseThrow(() -> new IllegalArgumentException("User with that name not exists: " + s));
        return repository.findById(userDto.getId()).get();
    }
}

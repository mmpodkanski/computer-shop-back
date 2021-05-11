package io.github.mmpodkanski.computershop.customer;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private final CustomerQueryRepository queryRepository;

    AuthService(final CustomerQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
        return queryRepository.findByUsername(s)
                .orElseThrow(() -> new IllegalArgumentException("User with that name not exists: " + s));
    }
}

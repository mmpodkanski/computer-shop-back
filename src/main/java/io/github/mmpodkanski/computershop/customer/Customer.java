package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.enums.ERole;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "customers")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Enumerated(EnumType.STRING)
    private ERole role;
    private boolean locked = false;
    private boolean enabled;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CustomerDetails details;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    @PersistenceConstructor
    protected Customer() {
    }

    Customer(final String email, final String username, final String password, final ERole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locked = false;
        this.enabled = true;
    }

    void addInfo(final CustomerDetails details) {
        this.details = details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(this.getRole().toString());
        return Collections.singletonList(authority);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ERole getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public CustomerDetails getDetails() {
        return details;
    }

    void setDetails(final CustomerDetails details) {
        this.details = details;
    }
}

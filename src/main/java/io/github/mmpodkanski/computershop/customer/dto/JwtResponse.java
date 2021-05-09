package io.github.mmpodkanski.computershop.customer.dto;

import java.util.Objects;

public class JwtResponse {
    private final int id;
    private final String token;
    private final String type = "Bearer";
    private final String email;
    private final String username;
    private final String role;

    public JwtResponse(
            final int id,
            final String accessToken,
            final String email,
            final String username,
            final String role
    ) {
        this.id = id;
        this.token = accessToken;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return id == that.id && Objects.equals(token, that.token) && Objects.equals(type, that.type) && Objects.equals(email, that.email) && Objects.equals(username, that.username) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, type, email, username, role);
    }
}

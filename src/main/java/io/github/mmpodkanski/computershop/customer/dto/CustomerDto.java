package io.github.mmpodkanski.computershop.customer.dto;

public interface CustomerDto {
    static CustomerDto create(
            final int id,
            final String username,
            final String email
    ) {
        return new CustomerDtoImpl(id, username, email);
    }

    int getId();

    String getUsername();

    String getEmail();

    class CustomerDtoImpl implements CustomerDto {
        private final int id;
        private final String username;
        private final String email;

        CustomerDtoImpl(final int id, final String username, final String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getEmail() {
            return email;
        }
    }
}

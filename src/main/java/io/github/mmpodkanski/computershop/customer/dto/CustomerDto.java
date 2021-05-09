package io.github.mmpodkanski.computershop.customer.dto;

public interface CustomerDto {
    static CustomerDto create(
            final int id,
            final String username,
            final String email,
            final CustomerDetailsDto details
    ) {
        return new CustomerDtoImpl(id, username, email, details);
    }

    int getId();

    String getUsername();

    String getEmail();

    CustomerDetailsDto getDetails();

    class CustomerDtoImpl implements CustomerDto {
        private final int id;
        private final String username;
        private final String email;
        private final CustomerDetailsDto details;

        CustomerDtoImpl(final int id, final String username, final String email, final CustomerDetailsDto details) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.details = details;
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

        @Override
        public CustomerDetailsDto getDetails() {
            return details;
        }
    }
}

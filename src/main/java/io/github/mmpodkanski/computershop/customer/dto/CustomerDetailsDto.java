package io.github.mmpodkanski.computershop.customer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CustomerDetailsDto.CustomerDetailsDtoImpl.class)
public interface CustomerDetailsDto {
    static CustomerDetailsDto create(
            final int id,
            final String firstName,
            final String lastName,
            final String gender,
            final String addressLine1,
            final String addressLine2,
            final String phone,
            final String city,
            final String country
    ) {
        return new CustomerDetailsDtoImpl(id, firstName, lastName, gender, addressLine1, addressLine2, phone, city, country);
    }

    int getId();

    String getFirstName();

    String getLastName();

    String getGender();

    String getAddressLine1();

    String getAddressLine2();

    String getPhone();

    String getCity();

    String getCountry();

    class CustomerDetailsDtoImpl implements CustomerDetailsDto {
        private int id;
        private String firstName;
        private String lastName;
        private String gender;
        private String addressLine1;
        private String addressLine2;
        private String phone;
        private String city;
        private String country;

        public CustomerDetailsDtoImpl() {
        }

        public CustomerDetailsDtoImpl(
                final int id,
                final String firstName,
                final String lastName,
                final String gender,
                final String addressLine1,
                final String addressLine2,
                final String phone,
                final String city,
                final String country
        ) {
            this.id = id;
            this.gender = gender;
            this.firstName = firstName;
            this.lastName = lastName;
            this.addressLine1 = addressLine1;
            this.addressLine2 = addressLine2;
            this.phone = phone;
            this.city = city;
            this.country = country;
        }


        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getFirstName() {
            return firstName;
        }

        @Override
        public String getLastName() {
            return lastName;
        }

        @Override
        public String getGender() {
            return gender;
        }

        @Override
        public String getAddressLine1() {
            return addressLine1;
        }

        @Override
        public String getAddressLine2() {
            return addressLine2;
        }

        @Override
        public String getPhone() {
            return phone;
        }

        @Override
        public String getCity() {
            return city;
        }

        @Override
        public String getCountry() {
            return country;
        }
    }
}

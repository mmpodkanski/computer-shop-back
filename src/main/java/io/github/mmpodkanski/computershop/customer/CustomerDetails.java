package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.enums.EGender;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customer_details")
class CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private EGender gender;
    @NotBlank(message = "Please type an address!")
    private String addressLine1;
    private String addressLine2;
    @Size(min = 9, max = 12)
    private String phone;
    @NotBlank(message = "Please type name of city!")
    private String city;
    @NotBlank(message = "Please type name of country!")
    private String country;

    @PersistenceConstructor
    protected CustomerDetails() {
    }

    CustomerDetails(
            final String firstName,
            final String lastName,
            final EGender gender,
            final String addressLine1,
            final String addressLine2,
            final String phone,
            final String city,
            final String country
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.phone = phone;
        this.city = city;
        this.country = country;
    }

    int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    EGender getGender() {
        return gender;
    }

    void setGender(final EGender gender) {
        this.gender = gender;
    }

    String getAddressLine1() {
        return addressLine1;
    }

    void setAddressLine1(final String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    String getAddressLine2() {
        return addressLine2;
    }

    void setAddressLine2(final String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(final String phone) {
        this.phone = phone;
    }

    String getCity() {
        return city;
    }

    void setCity(final String city) {
        this.city = city;
    }

    String getCountry() {
        return country;
    }

    void setCountry(final String country) {
        this.country = country;
    }

}

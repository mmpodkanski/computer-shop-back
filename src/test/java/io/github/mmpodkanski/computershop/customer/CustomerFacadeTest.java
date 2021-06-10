package io.github.mmpodkanski.computershop.customer;

import io.github.mmpodkanski.computershop.customer.auth.JwtUtils;
import io.github.mmpodkanski.computershop.customer.dto.CustomerDetailsDto;
import io.github.mmpodkanski.computershop.customer.dto.JwtResponse;
import io.github.mmpodkanski.computershop.customer.dto.LoginRequest;
import io.github.mmpodkanski.computershop.customer.dto.RegisterRequest;
import io.github.mmpodkanski.computershop.customer.enums.ERole;
import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerFacadeTest {

    @Test
    @DisplayName("should throw ApiBadRequestException when customer used already existing username")
    void register_withAlreadyUsedUsername_throwsApiBadRequestException() {
        //given
        var registerReq = new RegisterRequest("email@email.com", "username", "password");
        //and
        var customerQueryRepository = queryRepositoryReturning(false, true);
        //system under test
        var toTest = new CustomerFacade(null, customerQueryRepository, null, null, null, null);

        //when + then
        assertThatThrownBy(() -> toTest.register(registerReq))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Username is already taken");
    }

    @Test
    @DisplayName("should throw ApiBadRequestException when customer used already existing email")
    void register_withAlreadyUsedEmail_throwsApiBadRequestException() {
        //given
        var registerReq = new RegisterRequest("email@email.com", "username", "password");
        //and
        var customerQueryRepository = queryRepositoryReturning(true, false);
        //system under test
        var toTest = new CustomerFacade(null, customerQueryRepository, null, null, null, null);

        //when + then
        assertThatThrownBy(() -> toTest.register(registerReq))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Email is already in use");
    }

    @Test
    @DisplayName("should create and save customer")
    void register_withCorrectData_createsAndSavesCustomer() {
        //given
        var registerReq = new RegisterRequest("email@email.com", "username", "password");
        //and
        var customerQueryRepository = queryRepositoryReturning(false, false);
        //and
        var passwordEncoder = passwordEncoder();
        //and
        var inMemoryCustomerRep = new InMemoryCustomerRepository();
        var countBeforeCall = inMemoryCustomerRep.count();
        //system under test
        var toTest = new CustomerFacade(inMemoryCustomerRep, customerQueryRepository, null, passwordEncoder, null, null);

        //when
        toTest.register(registerReq);

        //then
        assertThat(countBeforeCall + 1)
                .isEqualTo(inMemoryCustomerRep.count());
    }

    @Test
    @DisplayName("should return JwtResponse when customer used correct data")
    void login_withCorrectData_returnsJwtResponse() {
        //given
        var loginRequest = new LoginRequest("usernameTest", "password");
        //and
        var mockAuthManager = mock(AuthenticationManager.class);
        var principal = new Customer("email@email.com", loginRequest.getUsername(), loginRequest.getPassword(), ERole.ROLE_USER);
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, loginRequest.getPassword(), principal.getAuthorities());

        when(mockAuthManager.authenticate(isA(Authentication.class))).thenReturn(auth);
        //and
        var mockJwtUtils = mock(JwtUtils.class);
        var jwt = "jwtTest";
        when(mockJwtUtils.generateJwtToken(auth)).thenReturn(jwt);

        //when
        var toTest = new CustomerFacade(null, null, mockAuthManager, null, mockJwtUtils, null);

        //then
        assertThat(toTest.login(loginRequest)).isInstanceOf(JwtResponse.class)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("username", loginRequest.getUsername())
                .hasFieldOrPropertyWithValue("token", jwt);
    }


    @Test
    @DisplayName("should throw NullPointerException when customer used invalid login data")
    void login_withInvalidData_throwsNullPointerException() {
        //given
        var loginRequest = new LoginRequest("usernameTest2", "password");
        //and
        var mockAuthManager = mock(AuthenticationManager.class);
        //and
        var mockJwtUtils = mock(JwtUtils.class);

        //when
        var toTest = new CustomerFacade(null, null, mockAuthManager, null, mockJwtUtils, null);

        //then
        assertThatThrownBy(() -> toTest.login(loginRequest))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should save customer details and returns dto")
    void addDetails_toCustomerWithNullDetails_saveThemAndReturnsDto() {
        //given
        var detailsRequest = CustomerDetailsDto.create(
                0,
                "firstName",
                "lastName",
                "MAN",
                "example",
                "12",
                "500500500",
                "city",
                "Poland"
        );
        int id = detailsRequest.getId();
        //
        var customer = new Customer("email@email.com", "username", "password", ERole.ROLE_USER);
        //and
        var mockCustomerRepository = new InMemoryCustomerRepository();

        //when
        var toTest = new CustomerFacade(mockCustomerRepository, null, null, null, null, new CustomerFactory());
        var customerDetailsDto = toTest.addDetails(detailsRequest, customer);

        //then
        assertThat(customerDetailsDto).isInstanceOf(CustomerDetailsDto.class);
        assertThat(mockCustomerRepository.findById(id).map(Customer::getDetails).orElseThrow())
                .isInstanceOf(CustomerDetails.class)
                .hasFieldOrPropertyWithValue("phone", detailsRequest.getPhone());
    }

    @Test
    @DisplayName("should update already existing customer details and returns dto")
    void addDetails_toCustomerWithNotNullDetails_updateDetailsAndReturnsDto() {
        //given
        var detailsRequest = CustomerDetailsDto.create(
                0,
                "firstName",
                "lastName",
                "MAN",
                "example",
                "12",
                "500500500",
                "city",
                "Poland"
        );
        int id = detailsRequest.getId();
        //
        var customer = new Customer("email@email.com", "username", "password", ERole.ROLE_USER);
        //and
        var mockCustomerRepository = new InMemoryCustomerRepository();

        //when
        mockCustomerRepository.save(customer);
        var toTest = new CustomerFacade(mockCustomerRepository, null, null, null, null, new CustomerFactory());
        var customerDetailsDto = toTest.addDetails(detailsRequest, customer);

        //then
        assertThat(customerDetailsDto).isInstanceOf(CustomerDetailsDto.class);
        assertThat(mockCustomerRepository.findById(id).map(Customer::getDetails).orElseThrow())
                .isInstanceOf(CustomerDetails.class)
                .hasFieldOrPropertyWithValue("phone", detailsRequest.getPhone());
    }


    private CustomerQueryRepository queryRepositoryReturning(final boolean emailResult, final boolean usernameResult) {
        var mockCustomerRepository = mock(CustomerQueryRepository.class);
        when(mockCustomerRepository.existsByUsername(anyString())).thenReturn(usernameResult);
        when(mockCustomerRepository.existsByEmail(anyString())).thenReturn(emailResult);
        return mockCustomerRepository;
    }

    private PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(final CharSequence rawPassword) {
                return null;
            }

            @Override
            public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
                return false;
            }
        };
    }

    private static class InMemoryCustomerRepository implements CustomerRepository {

        private int index = 0;
        private Map<Integer, Customer> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public Optional<Customer> findById(final int id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public void save(final Customer entity) {
            if (entity.getId() == 0) {
                try {
                    var field = Customer.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, index++);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
        }

        @Override
        public void delete(final Customer entity) {
            map.values().remove(entity);
        }
    }
}
package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.product.ProductQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CartFacadeTest {

    @Test
    @DisplayName("should throw ApiNotFoundException when cart has invalid product id")
    void addToCart_withBadIdProduct_throwsApiNotFoundException() {
        //given
        var addToCartDto = new AddToCartDto(0, 10, 5);
        //and
        var mockProductQueryRepository = mock(ProductQueryRepository.class);
        var countBeforeCall = mockProductQueryRepository.count();

        //when
        var toTest = new CartFacade(null, null, mockProductQueryRepository, null, null);


        //then
        assertThatThrownBy(() -> toTest.addToCart(addToCartDto, null))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("not exists");

        assertThat(mockProductQueryRepository.count()).isEqualTo(countBeforeCall);
    }

    @Test
    @DisplayName("should add new cart when currently is not existing then saves and return dto")
    void addToCart_withCorrectIdProduct_andCurrentlyNotExistingCart_thenSavesAndReturnDto() {
    }

    @Test
    @DisplayName("should increase product quantity of cart when currently is existing then saves and return dto")
    void addToCart_withCorrectIdProduct_andCurrentlyExistingCart_thenSavesAndReturnDto() {
    }

    @Test
    @DisplayName("should throw ApiBadRequestException when product quantity is more than is available in stock")
    void addToCart_withCorrectIdProduct_andMoreQuantityThanIsAvailable_throwsApiBadRequestException() {
    }

    @Test
    @DisplayName("should only decrease quantity when actual has more than one")
    void decreasingCartItemQuantity_whenAmountIsHigherThanOne() {
        //given
        var cartItem = new CartItem(null, 10, null);
        var quantityBeforeCall = cartItem.getQuantity();
        //and
        var mockQueryRepository = mock(CartItemQueryRepository.class);
        when(mockQueryRepository.existsById(anyInt()))
                .thenReturn(true);
        when(mockQueryRepository.findByIdAndCustomer(anyInt(), any()))
                .thenReturn(Optional.of(cartItem));

        var mockRepository = new inMemoryCartItemRepository();

        //when
        var toTest = new CartFacade(mockRepository, mockQueryRepository, null, null, null);
        mockRepository.save(cartItem);
        toTest.deleteCartItem(0, null);

        //then
        assertThat(quantityBeforeCall - 1)
                .isEqualTo(mockQueryRepository.findByIdAndCustomer(0, null).get().getQuantity());

        assertThat(quantityBeforeCall - 1)
                .isEqualTo(mockRepository.findById(cartItem.getId()).get().getQuantity());
    }

    @Test
    @DisplayName("should delete item cart")
    void deleteCartItem_withQuantityEqualOne() {
        //given
        var cartItem = new CartItem(null, 1, null);
        //and
        var mockQueryRepository = mock(CartItemQueryRepository.class);
        when(mockQueryRepository.existsById(anyInt()))
                .thenReturn(true);
        when(mockQueryRepository.findByIdAndCustomer(anyInt(), any()))
                .thenReturn(Optional.of(cartItem));


        var mockRepository = new inMemoryCartItemRepository();
        mockRepository.save(cartItem);

        //when
        var toTest = new CartFacade(mockRepository, mockQueryRepository, null, null, null);
        toTest.deleteCartItem(cartItem.getId(), null);

        //then
        assertThat(mockRepository.findById(cartItem.getId()))
                .isEmpty();
    }

    static class inMemoryCartItemRepository implements CartItemRepository {
        private int index = 0;
        private Map<Integer, CartItem> map = new HashMap<>();

        @Override
        public Optional<CartItem> findById(final int id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public CartItem save(final CartItem entity) {
            if (entity.getId() == 0) {
                try {
                    var field = CartItem.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, index++);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            return map.put(entity.getId(), entity);
        }

        @Override
        public void deleteByIdAndCustomer(final int id, final Customer customer) {
            map.remove(id);
        }

        @Override
        public void deleteAllByCustomer(final Customer customer) {
        }
    }
}
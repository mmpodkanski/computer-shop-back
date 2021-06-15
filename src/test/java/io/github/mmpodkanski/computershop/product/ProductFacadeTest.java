package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import io.github.mmpodkanski.computershop.product.dto.ProductRequest;
import io.github.mmpodkanski.computershop.product.enums.ECategory;
import io.github.mmpodkanski.computershop.product.enums.ECondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductFacadeTest {


    @Test
    @DisplayName("should update product and return dto")
    void updateProduct_andReturnDto() {
        //given
        var product = new Product(
                0,
                "name",
                "description",
                "code",
                ECategory.CPU,
                BigDecimal.ZERO,
                ECondition.NEW,
                1,
                null
        );

        var request = new ProductRequest(
                "nameUpdated",
                "descriptionUpdated",
                "codeUpdated",
                "GPU",
                BigDecimal.ZERO,
                "USED",
                3,
                null
        );
        //and
        var mockProductRepository = mock(ProductRepository.class);
        when(mockProductRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(product));
        when(mockProductRepository.save(any(Product.class)))
                .thenReturn(product);

        //when
        var toTest = new ProductFacade(mockProductRepository, null, new ProductFactory());
        var productDto = toTest.updateProduct(request, 0);

        //then
        assertThat(mockProductRepository.findById(0).get())
                .hasFieldOrPropertyWithValue("name", request.getName())
                .hasFieldOrPropertyWithValue("category", ECategory.valueOf(request.getCategory()));
        assertThat(productDto).isInstanceOf(ProductDto.class)
                .hasFieldOrPropertyWithValue("name", request.getName());
    }

    @Test
    @DisplayName("should increase product stock")
    void increaseProductStock_withCorrectValue() {
        //given
        var product = new Product(
                0,
                "name",
                "description",
                "code",
                ECategory.CPU,
                BigDecimal.ZERO,
                ECondition.NEW,
                1,
                null
        );
        var quantityBeforeCall = product.getQuantity();
        //and
        var mockProductRepository = mock(ProductRepository.class);
        when(mockProductRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(product));

        //when
        var toTest = new ProductFacade(mockProductRepository, null, null);
        toTest.increaseProductStock(product.getId(), 1);

        //then
        assertThat(quantityBeforeCall + 1)
                .isEqualTo(mockProductRepository.findById(product.getId()).get().getQuantity());
    }

    @Test
    @DisplayName("should decrease product stock")
    void decreaseProductStock_withCorrectValue() {
        //given
        var product = new Product(
                0,
                "name",
                "description",
                "code",
                ECategory.CPU,
                BigDecimal.ZERO,
                ECondition.NEW,
                1,
                null
        );
        var quantityBeforeCall = product.getQuantity();
        //and
        var mockProductRepository = mock(ProductRepository.class);
        when(mockProductRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(product));

        //when
        var toTest = new ProductFacade(mockProductRepository, null, null);
        toTest.decreaseProductStock(product.getId(), 1);

        //then
        assertThat(quantityBeforeCall - 1)
                .isEqualTo(mockProductRepository.findById(product.getId()).get().getQuantity());
    }

    @Test
    @DisplayName("should throw ApiBadRequestException when decrease more than is available")
    void decreaseProductStock_withMoreThanIsAvailable_throwsApiBadRequestException() {
        //given
        var product = new Product(
                0,
                "name",
                "description",
                "code",
                ECategory.CPU,
                BigDecimal.ZERO,
                ECondition.NEW,
                1,
                null
        );
        var quantityBeforeCall = product.getQuantity();
        //and
        var mockProductRepository = mock(ProductRepository.class);
        when(mockProductRepository.findById(anyInt()))
                .thenReturn(java.util.Optional.of(product));

        //when
        var toTest = new ProductFacade(mockProductRepository, null, null);

        //then
        assertThatThrownBy(() -> toTest.decreaseProductStock(product.getId(), product.getId() + quantityBeforeCall+1))
                .isInstanceOf(ApiBadRequestException.class);
    }
}
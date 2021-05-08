package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import io.github.mmpodkanski.computershop.product.enums.ECategory;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductQueryRepository extends Repository<Product, Integer> {
    boolean existsProductById(int productId);

    boolean existsProductByCode(String code);

    List<ProductDto> findAllByCode(String code);

    List<ProductDto> findAllDtoBy();

    List<ProductDto> findAllDtoByCategory(ECategory category);

    List<ProductDto> findAllDtoByCode(String code);

    Optional<ProductDto> findDtoById(int id);

    int count();
}

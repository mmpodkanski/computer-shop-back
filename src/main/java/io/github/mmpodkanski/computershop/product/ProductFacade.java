package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import io.github.mmpodkanski.computershop.product.dto.ProductRequest;
import io.github.mmpodkanski.computershop.product.enums.ECategory;
import io.github.mmpodkanski.computershop.product.enums.ECondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductFacade {
    private final ProductRepository repository;
    private final ProductQueryRepository queryRepository;
    private final ProductFactory productFactory;

    ProductFacade(final ProductRepository repository, final ProductQueryRepository queryRepository, final ProductFactory productFactory) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.productFactory = productFactory;
    }

    ProductDto addProduct(ProductRequest product) {
        var category = ECategory.valueOf(product.getCategory());

        Product result = queryRepository.findDtoByCode(product.getCode())
                .filter(pd -> pd.getCondition().equals(product.getCondition()))
                .map(productFactory::toEntity)
                .orElseGet(() -> repository.save(productFactory.toEntity(product)));


        if (!result.getCategory().equals(category)) {
            throw new ApiBadRequestException("You are trying to add product with the same code but in other category!");
        }

        return updateProduct(product, result.getId());
    }

    ProductDto updateProduct(ProductRequest request, int productId) {
        var product = getProduct(productId);

        var condition = ECondition.valueOf(request.getCondition());
        var category = ECategory.valueOf(request.getCategory());

        product.update(
                request.getName(),
                request.getDescription(),
                request.getCode(),
                category,
                request.getPrice(),
                condition,
                request.getQuantity() == 0 ? 1 : request.getQuantity(),
                request.getImgLogoUrl()
        );

        return productFactory.toDto(repository.save(product));
    }


    @Transactional
    public void increaseProductStock(int productId, int quantity) {
        var result = getProduct(productId);

        result.increaseStock(quantity);
    }

    @Transactional
    public void decreaseProductStock(int productId, int quantity) {
        var result = getProduct(productId);

        if (result.getQuantity() < quantity) {
            throw new ApiBadRequestException("Out of stock product, we have only: " + result.getQuantity());
        }
        result.decreaseStock(quantity);
    }

    void deleteProduct(int productId) {
        var result = getProduct(productId);

        repository.delete(result);
    }

    private Product getProduct(final int productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ApiNotFoundException("Product with that id not exists!: " + productId));
    }
}

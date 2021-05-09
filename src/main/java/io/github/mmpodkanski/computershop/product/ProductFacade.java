package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import io.github.mmpodkanski.computershop.product.dto.ProductRequest;
import io.github.mmpodkanski.computershop.product.enums.ECategory;
import io.github.mmpodkanski.computershop.product.enums.ECondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        var condition = ECondition.valueOf(product.getCondition());
        var category = ECategory.valueOf(product.getCategory());

        List<Product> result = queryRepository.findAllByCode(product.getCode())
                .stream()
                .map(productFactory::toEntity)
                .filter(pd -> pd.getCondition().equals(condition))
                .collect(Collectors.toList());

        if (result.size() > 1) {
            throw new IllegalStateException("There are more than one product with the same code...");
        } else if (result.size() == 0) {
            return productFactory.toDto(repository.save(productFactory.toEntity(product)));
        }

        var foundProduct = result.get(0);
        if (!foundProduct.getCategory().equals(category)) {
            throw new IllegalStateException("You are trying add product with the same code but in other category!");
        }

        foundProduct.increaseStock(foundProduct.getQuantity());
        return productFactory.toDto(repository.save(foundProduct));
    }

    @Transactional
    public void updateProduct(ProductRequest request, int productId) {
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
            throw new IllegalStateException("Out of stock product, we have only: " + result.getQuantity());
        }
        result.decreaseStock(quantity);
    }

    void deleteProduct(int productId) {
        var result = getProduct(productId);

        repository.delete(result);
    }

    private Product getProduct(final int productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with that id not exists!: " + productId));
    }
}

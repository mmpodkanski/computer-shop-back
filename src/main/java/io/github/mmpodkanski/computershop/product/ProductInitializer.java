package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.product.enums.ECategory;
import io.github.mmpodkanski.computershop.product.enums.ECondition;

import java.math.BigDecimal;

class ProductInitializer {
    private final ProductQueryRepository queryRepository;
    private final ProductRepository repository;

    ProductInitializer(final ProductQueryRepository queryRepository, final ProductRepository repository) {
        this.queryRepository = queryRepository;
        this.repository = repository;
    }

    void init() {
        if (queryRepository.count() == 0) {
            var product = new Product(
                    0,
                    "AMD 3600",
                    "świetny procesor",
                    "AMD_ASDASDAS",
                    ECategory.CPU,
                    BigDecimal.valueOf(123.3),
                    ECondition.NEW,
                    3,
                    null
            );

            repository.save(product);
        }
    }
}

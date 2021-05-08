package io.github.mmpodkanski.computershop.product;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class ProductWarmup implements ApplicationListener<ContextRefreshedEvent> {
    private final ProductInitializer initializer;

    ProductWarmup(final ProductRepository repository, final ProductQueryRepository queryRepository) {
        this.initializer = new ProductInitializer(queryRepository, repository);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initializer.init();
    }
}

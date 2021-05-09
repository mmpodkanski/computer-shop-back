package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.customer.CustomerFactory;
import io.github.mmpodkanski.computershop.product.ProductFactory;
import io.github.mmpodkanski.computershop.product.ProductQueryRepository;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartFacade {
    private final CartItemRepository repository;
    private final CartItemQueryRepository queryRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductFactory productFactory;
    private final CustomerFactory customerFactory;

    CartFacade(
            final CartItemRepository repository,
            final CartItemQueryRepository queryRepository,
            final ProductQueryRepository productQueryRepository,
            final ProductFactory productFactory,
            final CustomerFactory customerFactory
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.productQueryRepository = productQueryRepository;
        this.productFactory = productFactory;
        this.customerFactory = customerFactory;
    }

    public CartItemDto addToCart(AddToCartDto addToCartDto, Customer customer) {
        var productDto = checkCart(addToCartDto);

        var product = productFactory.toEntity(productDto);
        return toDto(repository.save(new CartItem(product, addToCartDto.getQuantity(), customer)));
    }

    // TODO: change repository method signature (should be only save methods with one exception)
    @Transactional
    public void updateCartItem(AddToCartDto addToCartDto, int cartId, Customer customer) {
        checkCart(addToCartDto);
        var cart = repository.findByIdAndCustomer(cartId, customer);
        cart.setQuantity(addToCartDto.getQuantity());
    }

    @Transactional
    public void deleteCartItem(int id, Customer customer) {
        if (!queryRepository.existsById(id))
            throw new IllegalArgumentException("Cart with that id not exists : " + id);

        repository.deleteByIdAndCustomer(id, customer);
    }

    @Transactional
    public void deleteCartItems(Customer customer) {
        repository.deleteAllByCustomer(customer);
    }


    private ProductDto checkCart(AddToCartDto addToCartDto) {
        var productId = addToCartDto.getProductId();
        var productDto = productQueryRepository.findDtoById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with that id not exists: " + productId));

        if (productDto.getQuantity() < addToCartDto.getQuantity()) {
            throw new IllegalStateException("You cant add more product than is available!");
        }

        return productDto;
    }

    private CartItemDto toDto(CartItem entity) {
        return CartItemDto.create(
                entity.getId(),
                customerFactory.toDto(entity.getCustomer()),
                productFactory.toDto(entity.getProduct()),
                entity.getQuantity()
        );
    }

}

package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.product.ProductFactory;
import io.github.mmpodkanski.computershop.product.ProductQueryRepository;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CartFacade {
    private final CartItemRepository repository;
    private final CartItemQueryRepository queryRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductFactory productFactory;

    CartFacade(
            final CartItemRepository repository,
            final CartItemQueryRepository queryRepository,
            final ProductQueryRepository productQueryRepository,
            final ProductFactory productFactory
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.productQueryRepository = productQueryRepository;
        this.productFactory = productFactory;
    }

    public CartItemDto addToCart(AddToCartDto addToCartDto, int customerId) {
        var productDto = checkCart(addToCartDto);

        var product = productFactory.dtoToEntity(productDto);
        return toDto(repository.save(new CartItem(product, addToCartDto.getQuantity(), customerId)));
    }

    @Transactional
    public void updateCartItem(AddToCartDto addToCartDto, int cartId, int customerId) {
        checkCart(addToCartDto);
        var cart = repository.findByIdAndCustomerId(cartId, customerId);
        cart.setQuantity(addToCartDto.getQuantity());
    }

    @Transactional
    public void deleteCartItem(int id, int customerId) {
        if (!queryRepository.existsById(id))
            throw new IllegalArgumentException("Cart with that id not exists : " + id);

        repository.deleteByIdAndCustomerId(id, customerId);
    }

    @Transactional
    public void deleteCartItems(int userId) {
        repository.deleteAllByCustomerId(userId);
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
                entity.getCustomerId(),
                productFactory.toDto(entity.getProduct()),
                entity.getQuantity()
        );
    }

}

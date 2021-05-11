package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.product.ProductQueryRepository;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartFacade {
    private final CartItemRepository repository;
    private final CartItemQueryRepository queryRepository;
    private final ProductQueryRepository productQueryRepository;
    private final CartFactory cartFactory;

    CartFacade(
            final CartItemRepository repository,
            final CartItemQueryRepository queryRepository,
            final ProductQueryRepository productQueryRepository,
            final CartFactory cartFactory
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.productQueryRepository = productQueryRepository;
        this.cartFactory = cartFactory;
    }

    public CartItemDto addToCart(AddToCartDto addToCartDto, Customer customer) {
        var productDto = getProductFromCart(addToCartDto);

        var itemCart = cartFactory.toEntity(addToCartDto, customer, productDto);
        return cartFactory.toDto(repository.save(itemCart));
    }

    // TODO: change repository method signature (should be only save methods with one exception)
    @Transactional
    public void updateCartItem(AddToCartDto addToCartDto, int cartId, Customer customer) {
        var productDto = getProductFromCart(addToCartDto);
        var cart = queryRepository.findByIdAndCustomer(cartId, customer);

        if (!productDto.getCode().equals(cart.getProduct().getCode())) {
            throw new ApiBadRequestException("Product is other than before!");
        }
        cart.setQuantity(addToCartDto.getQuantity());
    }

    @Transactional
    public void deleteCartItem(int id, Customer customer) {
        if (!queryRepository.existsById(id))
            throw new ApiNotFoundException("Cart with that id not exists : " + id);

        repository.deleteByIdAndCustomer(id, customer);
    }

    @Transactional
    public void deleteCartItems(Customer customer) {
        repository.deleteAllByCustomer(customer);
    }


    private ProductDto getProductFromCart(AddToCartDto addToCartDto) {
        var productId = addToCartDto.getProductId();
        var productDto = productQueryRepository.findDtoById(productId)
                .orElseThrow(() -> new ApiNotFoundException("Product with that id not exists: " + productId));

        if (productDto.getQuantity() < addToCartDto.getQuantity()) {
            throw new ApiBadRequestException("You cant add more product than is available!");
        }
        return productDto;
    }

}

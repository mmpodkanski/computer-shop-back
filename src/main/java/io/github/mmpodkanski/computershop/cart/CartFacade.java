package io.github.mmpodkanski.computershop.cart;

import io.github.mmpodkanski.computershop.cart.dto.AddToCartDto;
import io.github.mmpodkanski.computershop.cart.dto.CartItemDto;
import io.github.mmpodkanski.computershop.customer.Customer;
import io.github.mmpodkanski.computershop.exception.ApiBadRequestException;
import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.product.Product;
import io.github.mmpodkanski.computershop.product.ProductFactory;
import io.github.mmpodkanski.computershop.product.ProductQueryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartFacade {
    private final CartItemRepository repository;
    private final CartItemQueryRepository queryRepository;
    private final ProductQueryRepository productQueryRepository;
    private final CartFactory cartFactory;
    private final ProductFactory productFactory;

    CartFacade(
            @Qualifier("cartItemRepository") final CartItemRepository repository,
            final CartItemQueryRepository queryRepository,
            final ProductQueryRepository productQueryRepository,
            final CartFactory cartFactory,
            final ProductFactory productFactory
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.productQueryRepository = productQueryRepository;
        this.cartFactory = cartFactory;
        this.productFactory = productFactory;
    }

    CartItemDto addToCart(AddToCartDto addToCartDto, Customer customer) {
        var product = productQueryRepository.findById(addToCartDto.getProductId())
                .orElseThrow(() -> new ApiNotFoundException("Product with that id not exists: " + addToCartDto.getProductId()));

        var cartItem = queryRepository.findByProductAndCustomer(product, customer)
                .map(cart -> {
                    int finalQuantity = addToCartDto.getQuantity() + cart.getQuantity();
                    checkIfProductIsAvailable(product, finalQuantity);
                    cart.setQuantity(finalQuantity);
                    return cart;
                })
                .orElseGet(() -> {
                    checkIfProductIsAvailable(product, addToCartDto.getQuantity());
                    return cartFactory.toEntity(addToCartDto, customer, productFactory.toDto(product));
                });

        return cartFactory.toDto(repository.save(cartItem));
    }

    @Transactional
    public void deleteCartItem(int id, Customer customer) {
        if (!queryRepository.existsById(id)) {
            throw new ApiNotFoundException("Cart with that id not exists: " + id);
        }

        queryRepository.findByIdAndCustomer(id, customer)
                .ifPresent(cart -> {
                    if(cart.getQuantity() > 1) {
                        cart.decreaseQuantity();
                    } else {
                        repository.deleteByIdAndCustomer(id, customer);
                    }
                });
    }

    @Transactional
    public void deleteAllCartItems(Customer customer) {
        repository.deleteAllByCustomer(customer);
    }

    private void checkIfProductIsAvailable(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new ApiBadRequestException("You cant add more product than is available!");
        }
    }

}

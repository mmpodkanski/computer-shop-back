package io.github.mmpodkanski.computershop.product;

import io.github.mmpodkanski.computershop.exception.ApiNotFoundException;
import io.github.mmpodkanski.computershop.product.dto.ProductDto;
import io.github.mmpodkanski.computershop.product.dto.ProductRequest;
import io.github.mmpodkanski.computershop.product.enums.ECategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductFacade productFacade;
    private final ProductQueryRepository queryRepository;

    ProductController(final ProductFacade productFacade, final ProductQueryRepository queryRepository) {
        this.productFacade = productFacade;
        this.queryRepository = queryRepository;
    }

    @GetMapping()
    ResponseEntity<List<ProductDto>> readAllProducts(@RequestParam(required = false, defaultValue = "0") int page) {
        logger.info("Displaying all the products!");
        var products = queryRepository
                .findAllDtoBy(PageRequest.of(
                                page, 9,
                                Sort.by("quantity").ascending().and(Sort.by("price")).ascending()
                ));
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(params = "category")
    ResponseEntity<List<ProductDto>> readAllProductsByCategory(@RequestParam String stringCategory) {
        logger.info("Displaying products by category: " + stringCategory);
        var category = ECategory.valueOf(stringCategory);
        var products = queryRepository.findAllDtoByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(params = "name")
    ResponseEntity<List<ProductDto>> readAllProductByName(@RequestParam String name) {
        logger.info("Displaying products by name: " + name);
        var product = queryRepository.findAllDtoByNameContaining(name);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> readProductById(@PathVariable int id) {
        logger.info("Displaying product!");
        var product = queryRepository.findDtoById(id)
                .orElseThrow(() -> new ApiNotFoundException("Product with that id not exists: " + id));
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequest product) {
        logger.warn("Adding a new product...");
        var products = productFacade.addProduct(product);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProduct(
            @Valid @RequestBody ProductRequest product,
            @PathVariable int id
    ) {
        logger.warn("Updating product with id: " + id);
        var result = productFacade.updateProduct(product, id);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{id}", params = "increase")
    ResponseEntity<ProductDto> addProductToStock(
            @PathVariable int id,
            @RequestParam(value = "increase") int quantity
    ) {
        logger.warn("Increasing stock value of product " + id);
        productFacade.increaseProductStock(id, quantity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{id}", params = "decrease")
    ResponseEntity<ProductDto> removeProductFromStock(
            @PathVariable int id,
            @RequestParam(value = "decrease") int quantity
    ) {
        logger.warn("Increasing stock value of product " + id);
        productFacade.decreaseProductStock(id, quantity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<List<Void>> deleteProduct(@PathVariable int id) {
        productFacade.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

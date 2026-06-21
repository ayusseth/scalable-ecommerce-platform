package com.ayush.ecommerce.module.product.service;


import com.ayush.ecommerce.exception.CategoryNotFoundException;
import com.ayush.ecommerce.exception.ProductNotFoundException;
import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.dto.UpdateProductRequest;
import com.ayush.ecommerce.module.product.entity.Category;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.CategoryRepository;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @CacheEvict(
            value = {"products","product"},
            allEntries = true
    )
    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(()-> new CategoryNotFoundException("Category not found"));
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(category)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    @Cacheable("products")
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(product ->
                        ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .stockQuantity(product.getStockQuantity())
                                .active(product.isActive())
                                .categoryId(product.getCategory().getId())
                                .categoryName(product.getCategory().getName())
                                .build()
                ).toList();
    }

    @Override
    @Cacheable(value = "product", key = "#productId")
    public ProductDetailResponse getProductById(Long productId) {
        System.out.println(
                "Fetching product from DATABASE..."
        );
        Product product = productRepository
                .findByIdAndActiveTrue(productId)
                .orElseThrow(()->new ProductNotFoundException(
                        "Product not found"
                ));
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .categoryId(product.getCategory().getId()                )
                .categoryName(product.getCategory().getName()                )
                .build();
    }

    @Override
    @CacheEvict(
            value = {"products","product"},
            allEntries = true
    )
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(()->
                        new RuntimeException(
                                "Product not found"
                        ));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        product.setUpdatedAt(
                LocalDateTime.now()
        );
        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    //soft delete method is below
    @Override
    @CacheEvict(
            value = {"products","product"},
            allEntries = true
    )
    public void deleteProduct(Long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(()->new RuntimeException(
                        "Product not found"
                ));
        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    private ProductResponse mapToResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }

    @Override
    @Cacheable(
            value = "products",
            key = "#page + '-' + #size + '-' + #sortBy + '-' + #direction"
    )
    public Page<ProductResponse> getProducts(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository
                .findByActiveTrue(pageable)
                .map(this::mapToResponse);
    }


    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository
                .findByNameContainingIgnoreCaseAndActiveTrue(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }


    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository
                .findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> filterProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {

        List<Product> products;

        if (categoryId != null) {

            products = productRepository
                    .findByCategoryIdAndPriceBetweenAndActiveTrue(
                            categoryId,
                            minPrice,
                            maxPrice
                    );

        } else {

            products = productRepository
                    .findByPriceBetweenAndActiveTrue(
                            minPrice,
                            maxPrice
                    );
        }

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getLowStockProducts(Integer threshold) {
        return productRepository
                .findByStockQuantityLessThanAndActiveTrue(threshold)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}

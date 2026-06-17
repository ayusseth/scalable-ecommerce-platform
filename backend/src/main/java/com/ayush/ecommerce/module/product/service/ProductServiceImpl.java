package com.ayush.ecommerce.module.product.service;


import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.dto.UpdateProductRequest;
import com.ayush.ecommerce.module.product.entity.Category;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.CategoryRepository;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
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

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .stockQuantity(savedProduct.getStockQuantity())
                .active(savedProduct.isActive())
                .categoryId(savedProduct.getCategory().getId())
                .categoryName(savedProduct.getCategory().getName())
                .build();
    }

    @Override
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
    public ProductDetailResponse getProductById(Long productId) {
        Product product = productRepository
                .findByIdAndActiveTrue(productId)
                .orElseThrow(()->new RuntimeException(
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

        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .stockQuantity(updatedProduct.getStockQuantity())
                .active(updatedProduct.isActive())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }

    //soft delete method is below
    @Override
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
}

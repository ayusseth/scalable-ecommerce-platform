package com.ayush.ecommerce.module.product.service;


import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
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
                                .build()
                ).toList();
    }

    @Override
    public ProductDetailResponse getProductById(Long productId) {
        Product product = productRepository
                .findById(productId)
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
                .build();
    }
}

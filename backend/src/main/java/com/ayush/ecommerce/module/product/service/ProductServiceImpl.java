package com.ayush.ecommerce.module.product.service;


import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}

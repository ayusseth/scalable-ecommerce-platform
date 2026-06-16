package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductDetailResponse getProductById(Long productId);
}

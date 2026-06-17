package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.dto.UpdateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductDetailResponse getProductById(Long productId);

    ProductResponse updateProduct(Long productId, UpdateProductRequest request);

    void deleteProduct(Long productId);

    Page<ProductResponse> getProducts(
            int page,
            int size,
            String sortBy,
            String direction
    );

    List<ProductResponse> searchProducts(String keyword);

    List<ProductResponse> getProductsByCategory(Long categoryId);
}

package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.module.product.dto.CreateProductRequest;
import com.ayush.ecommerce.module.product.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);
}

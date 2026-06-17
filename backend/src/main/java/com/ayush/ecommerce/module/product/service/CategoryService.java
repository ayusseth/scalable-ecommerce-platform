package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.module.product.dto.CategoryResponse;
import com.ayush.ecommerce.module.product.dto.CreateCategoryRequest;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);
}

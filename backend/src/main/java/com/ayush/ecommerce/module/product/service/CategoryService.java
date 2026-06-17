package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.module.product.dto.CategoryResponse;
import com.ayush.ecommerce.module.product.dto.CreateCategoryRequest;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest request);

    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long categoryId);
    CategoryResponse updateCategory(Long categoryId, CreateCategoryRequest request);
    void deleteCategory(Long categoryId);
}

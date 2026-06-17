package com.ayush.ecommerce.module.product.controller;

import com.ayush.ecommerce.module.product.dto.CategoryResponse;
import com.ayush.ecommerce.module.product.dto.CreateCategoryRequest;
import com.ayush.ecommerce.module.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse createCategory(
            @Valid @RequestBody
            CreateCategoryRequest request
    ){
        return categoryService.createCategory(request);
    }
}

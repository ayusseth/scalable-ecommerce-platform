package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.exception.CategoryAlreadyExistsException;
import com.ayush.ecommerce.module.product.dto.CategoryResponse;
import com.ayush.ecommerce.module.product.dto.CreateCategoryRequest;
import com.ayush.ecommerce.module.product.entity.Category;
import com.ayush.ecommerce.module.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if(categoryRepository.existsByName(request.getName())){
            throw new CategoryAlreadyExistsException(
                    "Category already exists"
            );
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Category savedCategory = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .build();
    }
}

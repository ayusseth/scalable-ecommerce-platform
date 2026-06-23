package com.ayush.ecommerce.module.product.service;

import com.ayush.ecommerce.exception.CategoryAlreadyExistsException;
import com.ayush.ecommerce.module.product.dto.CategoryResponse;
import com.ayush.ecommerce.module.product.dto.CreateCategoryRequest;
import com.ayush.ecommerce.module.product.entity.Category;
import com.ayush.ecommerce.module.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    @CacheEvict(
            value = "categories",
            allEntries = true
    )
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

    @Override
    @Cacheable("categories")
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build()
                )
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(()->
                        new RuntimeException("Category not found"));

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    @CacheEvict(
            value = "categories",
            allEntries = true
    )
    public CategoryResponse updateCategory(Long categoryId, CreateCategoryRequest request) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(()->
                        new RuntimeException("Category not found"));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category updatedCategory = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(updatedCategory.getId())
                .name(updatedCategory.getName())
                .description(updatedCategory.getDescription())
                .build();
    }

    @Override
    @CacheEvict(
            value = "categories",
            allEntries = true
    )
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(()-> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryResponse> getCategories() {

        return categoryRepository
                .findAll()
                .stream()
                .map(category ->
                        CategoryResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .description(category.getDescription())
                                .build()
                )
                .toList();
    }
}

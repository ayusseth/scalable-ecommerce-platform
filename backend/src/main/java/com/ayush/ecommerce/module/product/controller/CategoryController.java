package com.ayush.ecommerce.module.product.controller;

import com.ayush.ecommerce.module.product.dto.CategoryResponse;
import com.ayush.ecommerce.module.product.dto.CreateCategoryRequest;
import com.ayush.ecommerce.module.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


   @GetMapping
    public List<CategoryResponse> getCategories() {

        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(
            @PathVariable Long id
    ) {

        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public CategoryResponse createCategory(
            @Valid @RequestBody
            CreateCategoryRequest request
    ){
        return categoryService.createCategory(request);
    }
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id,
                                           @Valid @RequestBody CreateCategoryRequest request){
        return categoryService.updateCategory(id, request);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategories(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }
}

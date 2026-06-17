package com.ayush.ecommerce.module.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank
    private String name;

    private String description;
}

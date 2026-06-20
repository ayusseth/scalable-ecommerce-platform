package com.ayush.ecommerce.module.product.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CategoryResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
}

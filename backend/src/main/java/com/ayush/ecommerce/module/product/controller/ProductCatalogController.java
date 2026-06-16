package com.ayush.ecommerce.module.product.controller;


//public API

import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCatalogController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDetailResponse getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}

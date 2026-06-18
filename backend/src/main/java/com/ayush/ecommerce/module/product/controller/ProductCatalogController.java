package com.ayush.ecommerce.module.product.controller;


//public API

import com.ayush.ecommerce.module.product.dto.ProductDetailResponse;
import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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


    @GetMapping("/paged")
    public Page<ProductResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return productService.getProducts(page,size,sortBy, direction);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestParam String keyword){
        return productService.searchProducts(keyword);
    }



    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/filter")
    public List<ProductResponse> filterProducts(

            @RequestParam(required = false)
            Long categoryId,
            @RequestParam
            BigDecimal minPrice,
            @RequestParam
            BigDecimal maxPrice
    ) {

        return productService.filterProducts(
                categoryId,
                minPrice,
                maxPrice
        );
    }
}

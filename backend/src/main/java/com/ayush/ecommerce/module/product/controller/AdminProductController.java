package com.ayush.ecommerce.module.product.controller;


import com.ayush.ecommerce.module.product.dto.ProductResponse;
import com.ayush.ecommerce.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts(
            @RequestParam(defaultValue = "10")
            Integer threshold
    ){
        return productService.getLowStockProducts(threshold);
    }
}

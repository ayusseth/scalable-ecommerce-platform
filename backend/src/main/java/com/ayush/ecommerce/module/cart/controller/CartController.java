package com.ayush.ecommerce.module.cart.controller;

import com.ayush.ecommerce.module.cart.dto.AddToCartRequest;
import com.ayush.ecommerce.module.cart.dto.CartResponse;
import com.ayush.ecommerce.module.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public CartResponse addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request
    ) {
        return cartService.addToCart(
                authentication.getName(),
                request
        );
    }

    @GetMapping
    public CartResponse getCart(
            Authentication authentication
    ) {
        return cartService.getCart(
                authentication.getName()
        );
    }
}
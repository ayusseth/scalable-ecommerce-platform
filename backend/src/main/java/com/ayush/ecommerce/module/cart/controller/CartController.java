package com.ayush.ecommerce.module.cart.controller;

import com.ayush.ecommerce.module.cart.dto.AddToCartRequest;
import com.ayush.ecommerce.module.cart.dto.CartResponse;
import com.ayush.ecommerce.module.cart.dto.CheckoutResponse;
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

    @PutMapping("/items/{productId}")
    public CartResponse updateCartItem(
            Authentication authentication,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ){
        return cartService.updateCartItem(
                authentication.getName(),
                productId,
                quantity
        );
    }

    @DeleteMapping("/items/{productId}")
    public void removeCartItem(
            Authentication authentication,
            @PathVariable Long productId
    ){
        cartService.removeCartItem(
                authentication.getName(),
                productId
        );
    }

    @DeleteMapping("/clear")
    public void clearCart(
            Authentication authentication
    ){
        cartService.clearCart(
                authentication.getName()
        );
    }

    @PostMapping("/checkout")
    public CheckoutResponse checkout(
            Authentication authentication
    ){
        return cartService.checkout(
                authentication.getName()
        );
    }
}
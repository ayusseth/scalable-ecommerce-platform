package com.ayush.ecommerce.module.auth.controller;

import com.ayush.ecommerce.module.auth.dto.RegisterRequest;
import com.ayush.ecommerce.module.auth.dto.RegisterResponse;
import com.ayush.ecommerce.module.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request
            ){

        return authService.register(request);
    }

    // without @Valid
    //@NotBlank, @Email, @Size never executed
}

package com.ayush.ecommerce.module.auth.service;

import com.ayush.ecommerce.module.auth.dto.LoginRequest;
import com.ayush.ecommerce.module.auth.dto.LoginResponse;
import com.ayush.ecommerce.module.auth.dto.RegisterRequest;
import com.ayush.ecommerce.module.auth.dto.RegisterResponse;

public interface AuthService
{
    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest request);
}

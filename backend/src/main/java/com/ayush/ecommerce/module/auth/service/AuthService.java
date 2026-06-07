package com.ayush.ecommerce.module.auth.service;

import com.ayush.ecommerce.module.auth.dto.RegisterRequest;
import com.ayush.ecommerce.module.auth.dto.RegisterResponse;

public interface AuthService
{
    RegisterResponse register(RegisterRequest registerRequest);
}

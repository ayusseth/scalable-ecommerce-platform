package com.ayush.ecommerce.module.auth.service;

import com.ayush.ecommerce.module.auth.dto.*;

public interface AuthService
{
    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest request);

    void forgotPassword(
            ForgotPasswordRequest request
    );

    void resetPassword(
            ResetPasswordRequest request
    );
}

package com.ayush.ecommerce.module.auth.otp;

public interface OtpService {

    String generateOtp(String email);

    boolean verifyOtp(
            String email,
            String otp
    );
}
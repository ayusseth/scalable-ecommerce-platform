package com.ayush.ecommerce.module.auth.controller;

import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.auth.dto.*;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.otp.OtpService;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.auth.service.AuthService;
import com.ayush.ecommerce.module.notification.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController
{
    private final AuthService authService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request
            ){

        return authService.register(request);
    }

    // without @Valid
    //@NotBlank, @Email, @Size never executed

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
            ){
        System.out.println("Login API hit....");
        return authService.login(request);
    }

    @PostMapping("/send-otp")
    public String sendOtp(@Valid @RequestBody
                          SendOtpRequest request){
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if(user != null && user.isEmailVerified()){
            throw new IllegalStateException(
                    "Email is already verified"
            );
        }
        String otp = otpService.generateOtp(request.getEmail());

        emailService.sendEmail(request.getEmail(),
                "Email Verification OTP",
                """
                Your verification code is:
                %s
                This OTP will expire in 5 minutes.
                """
                        .formatted(otp));
        return "OTP sent successfully";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @Valid
            @RequestBody
            VerifyOtpRequest request
    ) {

        boolean valid =
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                );

        if(!valid){
            throw new IllegalArgumentException(
                    "Invalid or expired OTP"
            );
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "Email verified successfully";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid
            @RequestBody
            ForgotPasswordRequest request
    ) {

        authService.forgotPassword(request);

        return "Password reset OTP sent successfully";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @Valid
            @RequestBody
            ResetPasswordRequest request
    ) {

        authService.resetPassword(request);

        return "Password reset successfully";
    }
}

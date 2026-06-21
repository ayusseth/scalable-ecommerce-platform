package com.ayush.ecommerce.module.auth.service;

import com.ayush.ecommerce.common.enums.RoleName;
import com.ayush.ecommerce.exception.RoleNotFoundException;
import com.ayush.ecommerce.exception.UserAlreadyExistsException;
import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.auth.dto.*;
import com.ayush.ecommerce.module.auth.entity.Role;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.otp.OtpService;
import com.ayush.ecommerce.module.auth.repository.RoleRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.notification.service.EmailService;
import com.ayush.ecommerce.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final OtpService otpService;
    private final EmailService emailService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException(
                    "Email already exist"
            );
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(()->
                        new RoleNotFoundException(
                                "Default role not found"
                        )
                );

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .roles(Set.of(userRole))
                .enabled(false)
                .emailVerified(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);
        String otp =
                otpService.generateOtp(
                        savedUser.getEmail()
                );

        emailService.sendEmail(
                savedUser.getEmail(),
                "Email Verification OTP",
                """
                Your verification code is:
        
                %s
        
                This OTP will expire in 5 minutes.
                """
                        .formatted(otp)
        );

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .message("Registration successful. Please verify your email using the OTP sent to your inbox.")
                .build();

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        if (!user.isEmailVerified()) {
            throw new IllegalStateException(
                    "Please verify your email first"
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(
                request.getEmail()
        );

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        String otp =
                otpService.generateOtp(
                        user.getEmail()
                );

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset OTP",
                """
                Your password reset OTP is:
    
                %s
    
                This OTP will expire in 5 minutes.
                """
                        .formatted(otp)
        );
    }

    @Override
    public void resetPassword(
            ResetPasswordRequest request
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

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setUpdatedAt(
                java.time.LocalDateTime.now()
        );

        userRepository.save(user);
    }
}

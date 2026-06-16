package com.ayush.ecommerce.module.auth.service;

import com.ayush.ecommerce.common.enums.RoleName;
import com.ayush.ecommerce.exception.RoleNotFoundException;
import com.ayush.ecommerce.exception.UserAlreadyExistsException;
import com.ayush.ecommerce.module.auth.dto.LoginRequest;
import com.ayush.ecommerce.module.auth.dto.LoginResponse;
import com.ayush.ecommerce.module.auth.dto.RegisterRequest;
import com.ayush.ecommerce.module.auth.dto.RegisterResponse;
import com.ayush.ecommerce.module.auth.entity.Role;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.RoleRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
                .enabled(true)
                .build();
        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();

    }

    @Override
    public LoginResponse login(LoginRequest request) {
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
}

package com.ayush.ecommerce.module.auth.service;

import com.ayush.ecommerce.common.enums.RoleName;
import com.ayush.ecommerce.exception.RoleNotFoundException;
import com.ayush.ecommerce.exception.UserAlreadyExistsException;
import com.ayush.ecommerce.module.auth.dto.RegisterRequest;
import com.ayush.ecommerce.module.auth.dto.RegisterResponse;
import com.ayush.ecommerce.module.auth.entity.Role;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.RoleRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
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
}

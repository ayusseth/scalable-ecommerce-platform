package com.ayush.ecommerce.module.admin.service;

import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.admin.dto.AdminUserResponse;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl
        implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public List<AdminUserResponse> getUsers() {

        return userRepository.findAllWithRoles()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AdminUserResponse> searchUsers(
            String keyword
    ) {

        return userRepository
                .searchUsersWithRoles(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void enableUser(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void disableUser(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        user.setEnabled(false);

        userRepository.save(user);
    }

    private AdminUserResponse mapToResponse(
            User user
    ) {

        return AdminUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .emailVerified(
                        user.isEmailVerified()
                )
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role ->
                                        role.getName().name()
                                )
                                .collect(
                                        java.util.stream.Collectors.toSet()
                                )
                )
                .createdAt(
                        user.getCreatedAt()
                )
                .build();
    }
}
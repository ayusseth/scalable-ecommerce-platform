package com.ayush.ecommerce.module.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class AdminUserResponse {

    private Long id;

    private String name;

    private String email;

    private boolean enabled;

    private boolean emailVerified;

    private Set<String> roles;

    private LocalDateTime createdAt;
}
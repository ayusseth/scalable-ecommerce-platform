package com.ayush.ecommerce.module.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserProfileResponse {

    private Long id;

    private String name;

    private String email;

    private boolean emailVerified;

    private Set<String> roles;
}
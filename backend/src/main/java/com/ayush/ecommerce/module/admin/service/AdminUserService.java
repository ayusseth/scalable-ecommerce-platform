package com.ayush.ecommerce.module.admin.service;

import com.ayush.ecommerce.module.admin.dto.AdminUserResponse;

import java.util.List;

public interface AdminUserService {

    List<AdminUserResponse> getUsers();

    List<AdminUserResponse> searchUsers(
            String keyword
    );

    void enableUser(Long userId);

    void disableUser(Long userId);
}
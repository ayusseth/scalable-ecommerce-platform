package com.ayush.ecommerce.module.admin.controller;

import com.ayush.ecommerce.module.admin.dto.AdminUserResponse;
import com.ayush.ecommerce.module.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<AdminUserResponse> getUsers() {

        return adminUserService.getUsers();
    }

    @GetMapping("/search")
    public List<AdminUserResponse> searchUsers(
            @RequestParam String keyword
    ) {

        return adminUserService.searchUsers(
                keyword
        );
    }

    @PutMapping("/{id}/enable")
    public void enableUser(
            @PathVariable Long id
    ) {

        adminUserService.enableUser(id);
    }

    @PutMapping("/{id}/disable")
    public void disableUser(
            @PathVariable Long id
    ) {

        adminUserService.disableUser(id);
    }
}
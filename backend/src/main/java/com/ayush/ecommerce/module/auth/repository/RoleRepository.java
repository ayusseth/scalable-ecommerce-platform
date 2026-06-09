package com.ayush.ecommerce.module.auth.repository;

import com.ayush.ecommerce.common.enums.RoleName;
import com.ayush.ecommerce.module.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

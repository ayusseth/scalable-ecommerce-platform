package com.ayush.ecommerce.module.auth.repository;

import com.ayush.ecommerce.module.auth.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name,
            String email
    );

    @Query("""
       SELECT DISTINCT u
       FROM User u
       LEFT JOIN FETCH u.roles
       """)
    List<User> findAllWithRoles();

    @Query("""
       SELECT DISTINCT u
       FROM User u
       LEFT JOIN FETCH u.roles
       WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
       """)
    List<User> searchUsersWithRoles(
            @Param("keyword") String keyword
    );

}

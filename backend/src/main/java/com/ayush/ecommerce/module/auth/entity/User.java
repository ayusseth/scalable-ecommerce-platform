package com.ayush.ecommerce.module.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name ="users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
        }
        )
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default    //because primitive booleans default to false. Newly registered users should normally be enabled = true by default.
    @Column(nullable = false)
    private boolean enabled;
    //The enabled field allows us to deactivate accounts without deleting user records.
    // This helps preserve historical data such as orders and payments while preventing the user from authenticating.
    // It also supports use cases like account suspension, email verification, and employee off-boarding.

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}

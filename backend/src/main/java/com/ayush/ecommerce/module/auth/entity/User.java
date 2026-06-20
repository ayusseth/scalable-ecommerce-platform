package com.ayush.ecommerce.module.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name ="users")
@Getter
@Setter
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
    private boolean enabled = true;
    //The enabled field allows us to deactivate accounts without deleting user records.
    // This helps preserve historical data such as orders and payments while preventing the user from authenticating.
    // It also supports use cases like account suspension, email verification, and employee off-boarding.

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}

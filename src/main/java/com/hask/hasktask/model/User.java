package com.hask.hasktask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hask.hasktask.config.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "user_role")
    private Role role = Role.USER; // Defaulted to USER

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phone;

    @JsonIgnore
    @Column(name = "user_password")
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // Set current time if not already set
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now(); // Set current time if not already set
        }
    }
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();  // Directly use role's authorities
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password; // OTP is used as password for authentication
    }

    @Override
    public String getUsername() {
        return email;  // Email is the USERNAME used for JWT generation
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;  // No expiration for the account
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;  // Account is not locked by default
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credentials are not expired by default
    }
}

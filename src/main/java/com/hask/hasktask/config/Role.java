package com.hask.hasktask.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {

    // Registered-> Users
    USER(
            Set.of(
                    Permission.USER_READ,
                    Permission.USER_CREATE,
                    Permission.USER_UPDATE
            )
    );


    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(
                        permission -> new SimpleGrantedAuthority(permission.getPermission())
                )
                .collect(Collectors.toList());

        // Assign current role
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}

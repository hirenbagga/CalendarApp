package com.hask.hasktask.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    USER_READ("admin:read"),
    USER_CREATE("admin:create"),
    USER_UPDATE("admin:update"),
    USER_DELETE("admin:delete");

    private final String permission;
}

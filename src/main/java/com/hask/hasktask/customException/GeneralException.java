package com.hask.hasktask.customException;

import org.springframework.security.core.AuthenticationException;

public class GeneralException extends AuthenticationException {

    public GeneralException(String id, String message) {
        super(String.format("Failed for [%s]: %s", id, message));
    }

}

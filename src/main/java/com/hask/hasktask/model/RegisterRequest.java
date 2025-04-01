package com.hask.hasktask.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @Builder.Default
    private String otp = "";

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String role;

    private String password;
}

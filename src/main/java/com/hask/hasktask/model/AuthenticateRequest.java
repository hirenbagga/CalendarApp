package com.hask.hasktask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateRequest {

    /**
     * @apiNote : username -> Email or Client_IP
     */
    @JsonProperty("user_email")
    private String email;
    @JsonProperty("user_password")
    private String password;
}

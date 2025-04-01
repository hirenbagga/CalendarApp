package com.hask.hasktask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {

    /**
    * @apiNote
    * ACCESS_TOKEN has short lifespan.
    * REFRESH_TOKEN has long lifespan.
    * Hence, when ACCESS_TOKEN expires
    * REFRESH_TOKEN is used to get a new ACCESS_TOKEN
    * */
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("expiresIn")
    private Date accessExpiration;
    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonProperty("refreshExpiresIn")
    private Date refreshExpiration;
}

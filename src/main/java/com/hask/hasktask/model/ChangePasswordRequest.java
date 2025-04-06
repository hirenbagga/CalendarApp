package com.hask.hasktask.model;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {

    @Column(insertable = false, updatable = false)
    private Long id;
    private String newPassword;
    private String confirmPassword;
}

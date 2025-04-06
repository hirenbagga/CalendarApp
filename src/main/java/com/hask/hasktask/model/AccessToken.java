package com.hask.hasktask.model;

import com.hask.hasktask.service.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_token")
@EntityListeners(AuditingEntityListener.class)
public class AccessToken {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType tokenType;

    @Column(name = "access_expiration")
    private Date accessExpiration;

    @Column(columnDefinition = "boolean default false", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    // @Builder.Default
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; /* = LocalDateTime.now()*/

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (accessExpiration == null) {
            accessExpiration = new Date(
                    System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7
            ); // 7 days
        }
    }

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

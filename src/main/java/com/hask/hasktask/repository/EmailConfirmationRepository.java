package com.hask.hasktask.repository;

import com.hask.hasktask.model.EmailConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmation, Integer> {
    Optional<EmailConfirmation> findByConfirmationToken(String token);

    Optional<EmailConfirmation> findByOtpCode(String otpCode);
}

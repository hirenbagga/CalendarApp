package com.hask.hasktask.repository;

import com.hask.hasktask.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    /// @Modifying
    // int deleteByUser(User user);
}

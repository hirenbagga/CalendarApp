package com.hask.hasktask.repository;

import com.hask.hasktask.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);

    /// @Modifying
    // int deleteByUser(User user);
}

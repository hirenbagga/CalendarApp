package com.hask.hasktask.repository;


import com.hask.hasktask.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    // Get All Tokens from the Token TABLE
    /* * @Query("SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id " +
            "WHERE u.id = :userId AND (t.expired = FALSE OR t.revoked = FALSE )"
    )
    List<Token> finAllValidTokensByUser(Long userId);*/

    Optional<AccessToken> findByAccessToken(String token);

    Optional<AccessToken> findByUserId(Long userId);

    /// @Modifying
    // int deleteByUser(User user);
}

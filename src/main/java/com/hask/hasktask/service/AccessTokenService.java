package com.hask.hasktask.service;

import com.hask.hasktask.config.service.JWTService;
import com.hask.hasktask.model.AccessToken;
import com.hask.hasktask.model.User;
import com.hask.hasktask.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class AccessTokenService {

    private final JWTService jwtService;
    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public AccessTokenService(JWTService jwtService, AccessTokenRepository accessTokenRepository) {
        this.jwtService = jwtService;
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * @apiNote : Check if Token from Database is unexpired
     */
    public boolean isUnexpiredServerToken(String token) {
        return findByAccessToken(token)
                .map(t -> !t.getAccessExpiration().before(new Date()))
                .orElse(false);
        /* * boolean isTokenValid = accessTokenRepository.findByToken(refreshToken)
                    .map(t -> t.isExpired() && t.isRevoked())
                    .orElse(false);*/
    }

    public Optional<AccessToken> findByAccessToken(String token) {
        return accessTokenRepository.findByAccessToken(token);
    }

    public void deleteByToken(AccessToken storedToken) {
        accessTokenRepository.delete(storedToken);
    }

    // save new accessToken(JWT)
    public AccessToken save(User user) {
        Optional<AccessToken> ak = accessTokenRepository.findByUserId(user.getId());
        if (ak.isPresent() && ak.get().getAccessExpiration().after(new Date())) {
            AccessToken _accessToken = ak.get();

            // Return old unExpired AccessToken
            return getBuild(
                    user,
                    _accessToken.getAccessToken(),
                    _accessToken.getAccessExpiration()
            );
        }

        // else, AccessToken expired, hence Revoke old token from DB
        revokeOldAccessToken(user);

        // Generate AccessToken
        Map<String, Object> jwt = jwtService.generateJWTToken(user);

        // Return New AccessToken
        AccessToken token = getBuild(
                user,
                jwt.get("jwtAccessToken").toString(),
                (Date) jwt.get("jwtAccessExpiration")
        );

        return accessTokenRepository.save(token);
    }

    private AccessToken getBuild(User user, String token, Date expiryDate) {
        // emailSenderService.tokenNotification(user.getEmail(), "Access");

        return AccessToken.builder()
                .user(user)
                .tokenType(TokenType.BEARER)
                .accessExpiration(expiryDate)
                .accessToken(token)
                .build();
    }

    private void revokeOldAccessToken(User user) {
        Optional<AccessToken> savedToken =
                accessTokenRepository.findByUserId(user.getId());

        if (savedToken.isEmpty()) return;

        deleteByToken(savedToken.get());
    }
}

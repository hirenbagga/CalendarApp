package com.hask.hasktask.service;

import com.hask.hasktask.config.service.JWTService;
import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.RefreshToken;
import com.hask.hasktask.model.User;
import com.hask.hasktask.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final JWTService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(JWTService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public Optional<RefreshToken> findByRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    // Is Refresh Token Expired
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getRefreshExpiration().before(new Date())) {
            // RefreshToken expired, so delete from DB
            refreshTokenRepository.delete(token);

            throw new GeneralException(token.getRefreshToken(), "Refresh token expired. Please make a new SignIn request");
        }

        return token;
    }

    public RefreshToken save(User user) {

        Optional<RefreshToken> rk = refreshTokenRepository.findByUserId(user.getId());
        if (rk.isPresent() && rk.get().getRefreshExpiration().after(new Date())) {
            RefreshToken _refreshToken = rk.get();

            // Return old unExpired RefreshToken
            return getBuild(
                    user,
                    _refreshToken.getRefreshToken(),
                    _refreshToken.getRefreshExpiration()
            );

            /* *throw new GeneralException(
                    rk.get().getRefreshToken(),
                    "Refresh token is not expired; " +
                            "use it as [Authorization header]" +
                            " to request new [Access Token]"
            );*/
        }

        // else, RefreshToken expired, hence Revoke old token from DB
        revokeOldRefreshToken(rk);

        // Generate New RefreshToken
        Map<String, Object> tk = jwtService.generateRefreshToken(user);

        // Return New RefreshToken
        RefreshToken token = getBuild(
                user,
                tk.get("jwtRefreshToken").toString(),
                (Date) tk.get("jwtRefreshExpiration")
        );

        // Return New RefreshToken
        return refreshTokenRepository.save(token);
    }

    private RefreshToken getBuild(User user, String token, Date expiryDate) {
        // emailSenderService.tokenNotification(user.getEmail(), "Refresh");

        return RefreshToken.builder()
                .user(user)
                .tokenType(TokenType.BEARER)
                .refreshExpiration(expiryDate)
                .refreshToken(token)
                .build();
    }

    private void revokeOldRefreshToken(Optional<RefreshToken> savedToken) {

        if (savedToken.isEmpty()) return;

        refreshTokenRepository.delete(savedToken.get());
    }

    public Optional<User> verifyRefreshToken(String refreshToken) {

        return findByRefreshToken(refreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                /*.map(user -> JWTResponse
                        .builder()
                        .accessToken(jwt.get("jwtAccessToken").toString())
                        .refreshToken(refreshToken)
                        .accessExpiration(jwt.get("jwtAccessExpiration").toString())
                        .build())
                .orElseThrow(
                        () -> new GeneralException(refreshToken, "Refresh token isn't associated to any account!")
                )*/;
    }
}

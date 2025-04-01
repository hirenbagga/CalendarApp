package com.hask.hasktask.config.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
 * JWT has two kind of tokens: ACCESS_TOKEN and REFRESH_TOKEN
 *
 * ACCESS_TOKEN (JWT_Token) ( has short lifespan -> 1 hour ):
 * -> ACCESS_TOKEN isn't saved in the Database
 * When a user logins in, the authorization server
 * issues an access token, which is an artifact that client
 * applications can use to make secure calls to an API server.
 *
 * REFRESH_TOKEN ( has long lifespan -> 24 hours / more):
 *  -> REFRESH_TOKEN is saved in the Database for reference CALL
 * So when ACCESS_TOKEN expires REFRESH_TOKEN is used to get a new ACCESS_TOKEN.
 * */
@Service
public class JWTService {

    /*
     * Random 256 bit 32 Byte Hex (HS256) Key: Generated online
     * REF: https://seanwasere.com/generate-random-hex
     * https://jwt.io
     * */
    @Value("${haskTask.app.jwtSecret}")
    private String SECRET_KEY;

    // JWT / Access Token Expiration: 1 HOUR = 3600000
    @Value("${haskTask.app.jwtExpirationMs}")
    private Long jwtExpirationMs;
    // new Date(System.currentTimeMillis() + 100 * 60 * 24);

    // RefreshToken Expiration: 7 DAYS / 168 HOURS = 604800000
    @Value("${haskTask.app.refreshExpirationMs}")
    private Long refreshExpirationMs;

    // EmailVerifyToken Expiration: 10 MINUTES = 600000
    @Value("${haskTask.app.emailVerifyExpirationMs}")
    private Long emailVerifyExpirationMs;

    private Date getTime;

    /*
     * Decode JWT / Access Token:
     * Extract Client/User details from their
     * Request payload (Authorization header)*/
    public String extractUsername(String jwtToken) {

        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);

        return claimsResolver.apply(claims);
    }

    private Map<String, Object> buildMap(
            UserDetails userDetails,
            Long expiryDate,
            String tKey,
            String eKey
    ) {
        Map<String, Object> map = new HashMap<>();
        String vToken = buildToken(new HashMap<>(), userDetails, expiryDate);

        map.put(tKey, vToken);
        map.put(eKey, getTime);

        return map;
    }

    public Map<String, Object> generateEmailVerifyToken(UserDetails userDetails) {

        return buildMap(userDetails, emailVerifyExpirationMs, "emailConfirmationToken", "emailConfirmationExpiration");
    }

    /**
     * @apiNote Generate New RefreshTokens with userDetails & extractClaims:
     * If JWT/ACCESS TOKEN expires, REFRESH_TOKEN is used to get a new ACCESS_TOKEN
     */
    public Map<String, Object> generateRefreshToken(UserDetails userDetails) {

        return buildMap(userDetails, refreshExpirationMs, "jwtRefreshToken", "jwtRefreshExpiration");
    }

    /**
     * @apiNote Generate New JWT-Token(Access Token) 'WITH ONLY userDetails'
     * Email is used as the USERNAME
     * to generate the Authorization (JWT-Token) for API Access
     */
    public Map<String, Object> generateJWTToken(UserDetails userDetails) {

        return buildMap(userDetails, jwtExpirationMs, "jwtAccessToken", "jwtAccessExpiration");
    }

    /*
     * @apiNote Generate New JWT-Token(Access Token) 'WITH userDetails & extractClaims'
    public String generateJWTToken(
            Map<String, Object> extractClaims,
            UserDetails userDetail) {

        return buildToken(extractClaims, userDetail, jwtExpirationMs);
    }*/

    private String buildToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails,
            Long expirationMs) {

        getTime = new Date(System.currentTimeMillis() + expirationMs);

        return Jwts
                .builder()
                .setClaims(extractClaims)
                /*
                 * Email is used as the USERNAME to generate the
                 * Authorization (JWT / Access Token) for API Access*/
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getTime)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate Token:
     * Check if JWT/Access Token belongs to this userDetails
     * and is not Expired:-> isTokenExpired()
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

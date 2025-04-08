package com.hask.hasktask.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hask.hasktask.config.Role;
import com.hask.hasktask.config.service.JWTService;
import com.hask.hasktask.customException.EntityNotFoundException;
import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.*;
import com.hask.hasktask.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenService verificationTokenService;

    /**
     * Register/SignUp a New API Client/User
     */
    public VerificationDetails register(RegisterRequest req,
                                        HttpServletRequest httpRequest) {

        String userEmail = req.getEmail();

        if (userExists(userEmail) || !verificationTokenService.isValidEmail(userEmail)) {
            throw new GeneralException(userEmail, "User with already exist");
        }

        // 1️⃣ Create and save the user
        var user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(userEmail)
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                // By default, set Role to USER, if not provided
                .role(Role.valueOf(req.getRole().toUpperCase()))
                .build();

        userRepository.save(user);

        // 2️⃣ Generate a verification OTP & token
        var verify = verificationTokenService.saveEmailVerification(user);

        return new VerificationDetails(
                verify.get("token").toString(),
                verify.get("otp").toString(),
                userEmail,
                "ACC_CREATED"
        );
    }

    /**
     * Resend Confirm Registered/SignUp Email
     */
    public VerificationDetails resendConfirmationMail(String email) {

        // 1️⃣ Check if user exists
        var user = findUserByEmail(email);

        // Is account not activated/verified?
        if (user.isEnabled()) {
            throw new GeneralException(email, "User account already activated");
        }

        // 2️⃣ Generate a verification OTP & token
        var verify = verificationTokenService.saveEmailVerification(user);

        return new VerificationDetails(
                verify.get("token").toString(),
                verify.get("otp").toString(),
                email,
                "ACC_RESEND"
        );
    }

    /**
     * Confirm Register/SignUp via Email Address
     */
    public void confirmAccountViaEmail(String token) {
        if (!StringUtils.hasText(token)) {
            throw new GeneralException(token, "Confirmation token is required");
        }
        verificationTokenService.confirmAccount(token);
    }

    public void confirmOTP(String otp) {
        if (!StringUtils.hasText(otp)) {
            throw new GeneralException(otp, "OTP code is required");
        }
        verificationTokenService.otpMatches(otp);
    }

    /**
     * Authenticate/SignIn API Client/User
     * RETURN -> Generate New AccessToken(JWT) & New RefreshToken
     */
    public JWTResponse authenticate(AuthenticateRequest authenticateRequest,
                                    HttpServletRequest httpRequest) {
        String username = authenticateRequest.getEmail();
        String password = authenticateRequest.getPassword();

        /* *UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password
        );
        boolean isAuthenticated = authenticationManager.authenticate(authenticationToken) != null;

        if (!isAuthenticated) {
            throw new EntityNotFoundException(User.class, "{password} ", password);
        }*/

        return getJWTResponse(username, password);
    }

    private JWTResponse getJWTResponse(String username, String password) {
        var user = findUserByEmail(username);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(), password
                )
        );

        // Save current LoggedIn Session
        // authMetricService.create(httpRequest, user);

        // save new refreshToken
        // If JWT/ACCESS TOKEN expires, REFRESH_TOKEN is used to generate a new ACCESS_TOKEN
        RefreshToken _refreshToken = refreshTokenService.save(user);

        // Response
        AccessToken _accessToken = accessTokenService.save(user);

        return JWTResponse.builder()
                .accessToken(_accessToken.getAccessToken())
                .accessExpiration(_accessToken.getAccessExpiration())
                .refreshToken(_refreshToken.getRefreshToken())
                .refreshExpiration(_refreshToken.getRefreshExpiration())
                .build();
    }

    /**
     * RETURN -> old RefreshToken & Generate New AccessToken(JWT)
     * REFRESH_TOKEN is used to generate a new ACCESS_TOKEN
     */
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        // System.out.println("Allowed Origin IP" + Utils.getLocalHostIp());

        // Get Authorization Token from Client Request payload Header
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String username;
        /*
         * Does Authorization Header has a Bearer Token
         * which start with "Bearer " from Client/User Request payload
         * */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        /*
         * Extract the Refresh Token from Client/User
         * Request payload (Authorization header -> "Bearer ") */
        refreshToken = authHeader.substring(7);

        username = jwtService.extractUsername(refreshToken);

        // Is username associated to RefreshToken
        if (username != null) {

            var user = findUserByEmail(username);

            // Is RefreshToken valid
            if (user.isEnabled() &&
                    jwtService.isTokenValid(refreshToken, user)) {

                // Generate new JWT/Access Token
                JWTResponse res = getRefreshJwtResponse(refreshToken);

                // Response
                new ObjectMapper()
                        .writeValue(response.getOutputStream(), res);
            }
        }

    }

    private JWTResponse getRefreshJwtResponse(String refreshToken) {
        return refreshTokenService.verifyRefreshToken(refreshToken)
                .flatMap(
                        verify -> refreshTokenService.findByRefreshToken(refreshToken)
                                .map(rK -> {
                                            AccessToken aK = accessTokenService.save(verify);

                                            return JWTResponse.builder()
                                                    .accessToken(aK.getAccessToken())
                                                    .accessExpiration(aK.getAccessExpiration())
                                                    .refreshToken(rK.getRefreshToken())
                                                    .refreshExpiration(rK.getRefreshExpiration())
                                                    .build();
                                        }
                                )
                )
                .orElseThrow(
                        () -> new GeneralException(refreshToken, "Refresh token isn't associated to any account!")
                );
    }


    public JWTResponse verifyResetPasswordOTP(String otp, String email) {
        if (!StringUtils.hasText(otp) || !StringUtils.hasText(email)) {
            throw new GeneralException(otp, "OTP code is required");
        }

        verificationTokenService.otpMatches(otp);
        return getJWTResponse(email, otp);
    }

    public VerificationDetails forgotPassword(String email) {
        var user = findUserByEmail(email);
        if (!user.isEnabled()) {
            throw new GeneralException(email, "User email invalid");
        }


        // 2️⃣ Generate a verification OTP & token
        var verify = verificationTokenService.saveEmailVerification(user);

        String otp = verify.get("otp").toString();

        // Update the password with Temporal OTP
        user.setPassword(passwordEncoder.encode(otp));
        user.setUpdatedAt(LocalDateTime.now()); // Last Updated Time
        userRepository.save(user); // save the new password

        return new VerificationDetails(
                verify.get("token").toString(),
                otp,
                email,
                "ACC_FORGOT"
        );
    }

    private User findUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(User.class, "{username} ", email)
                );
    }

    public Boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}

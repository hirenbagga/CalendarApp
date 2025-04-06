package com.hask.hasktask.service;

import com.hask.hasktask.config.service.JWTService;
import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.VerificationToken;
import com.hask.hasktask.model.User;
import com.hask.hasktask.repository.UserRepository;
import com.hask.hasktask.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    @Value("${haskTask.app.otpChar}")
    private String characters;

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private static final int OTP_LENGTH = 6;  // OTP length can be changed

    public void saveVerification(User user, String msg, String alt, Date expiry) {
        var confirmationToken = VerificationToken.builder()
                .user(user)
                .token(msg)
                .otp(alt)
                .expiryDate(expiry)
                .build();

        verificationTokenRepository.save(confirmationToken);
    }

    public void delete(Long id) {
        verificationTokenRepository.deleteById(id);
    }

    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public Optional<VerificationToken> findByOTP(String otp) {
        return verificationTokenRepository.findByOtp(otp);
    }

    public Map<String, Object> saveEmailVerification(User user) {

        // Generate OTP and email verification token
        String otp = generateOTP();
        Map<String, Object> tk = jwtService.generateEmailVerifyToken(user);

        // Get token and expiration date from Map
        String token = tk.get("emailConfirmationToken").toString();
        Date tokenExpiration = (Date) tk.get("emailConfirmationExpiration");

        saveVerification(user, token, otp, tokenExpiration);

        // Return a map with token, OTP, and email
        return Map.of(
                "otp", otp,
                "token", token
        );
    }

    public void otpMatches(String otp) {

        findByOTP(otp)
                .map(message -> {
                    var user = message.getUser();
                    user.setEnabled(true);
                    userRepository.save(user);

                    delete(message.getId());

                    return "redirect:/sign-in";
                })
                .orElseThrow(
                        () -> new GeneralException("OTP Confirmation", "Email confirmation OTP expired!")
                );
    }

    public void confirmAccount(String token) {

        findByToken(token)
                .map(notification -> {
                    final User user = notification.getUser();
                    user.setEnabled(true);
                    userRepository.save(user);

                    delete(notification.getId());

                    return "redirect:/sign-in";
                }).orElseThrow(
                        () -> new GeneralException("Account Activation", "Email confirmation token expired!")
                );
    }

    // Method to generate OTP
    public String generateOTP() {
        if (characters == null || characters.isEmpty()) {
            throw new GeneralException("OTP Failed", "Could not send OTP. Please contact support.");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        // Generate OTP of fixed length
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }

        return otp.toString();
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$";
        return StringUtils.hasText(email) && Pattern.matches(emailRegex, email);
    }

}

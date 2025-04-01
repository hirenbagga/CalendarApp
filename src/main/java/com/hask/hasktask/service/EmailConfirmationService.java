package com.hask.hasktask.service;

import com.hask.hasktask.config.service.JWTService;
import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.EmailConfirmation;
import com.hask.hasktask.model.User;
import com.hask.hasktask.repository.EmailConfirmationRepository;
import com.hask.hasktask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailConfirmationService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    public EmailConfirmationService(JWTService jwtService, UserRepository userRepository, EmailSenderService emailSenderService, EmailConfirmationRepository emailConfirmationRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.emailConfirmationRepository = emailConfirmationRepository;
    }

    public void sendConfirmationMail(User user) {

        String otp = OTPService.generateOTP();
        String token = saveEmailConfirmationToken(user, otp);

        emailSenderService.sendMail(token, otp, user.getEmail());
    }

    private String saveEmailConfirmationToken(User user, String otp) {

        Map<String, Object> tk = jwtService.generateEmailVerifyToken(user);

        String token = tk.get("emailConfirmationToken").toString();
        Date tokenExpiration = (Date) tk.get("emailConfirmationExpiration");


        var confirmationToken = EmailConfirmation.builder()
                .user(user)
                .confirmationToken(token)
                .otpCode(otp)
                .tokenExpiration(tokenExpiration)
                .build();
        emailConfirmationRepository.save(confirmationToken);

        return token;
    }

    public void otpMatches(String otp) {

        emailConfirmationRepository.findByOtpCode(otp)
                .map(otpConfirmation -> {
                    var user = otpConfirmation.getUser();
                    user.setEnabled(true);
                    userRepository.save(user);

                    deleteOTP(otpConfirmation.getId());

                    return "redirect:/sign-in";
                })
                .orElseThrow(
                        () -> new GeneralException("OTP Confirmation", "Email confirmation OTP expired!")
                );
    }

    public void confirmRegisteredEmail(String token) {

        findEmailConfirmationByToken(token)
                .map(emailConfirmation -> {
                    final User user = emailConfirmation.getUser();
                    user.setEnabled(true);
                    userRepository.save(user);

                    deleteEmailConfirmationToken(emailConfirmation.getId());

                    return "redirect:/sign-in";
                }).orElseThrow(
                        () -> new GeneralException("Account Activation", "Email confirmation token expired!")
                );
    }

    public void deleteOTP(Integer id) {

        emailConfirmationRepository.deleteById(id);
    }

    public void deleteEmailConfirmationToken(Integer id) {

        emailConfirmationRepository.deleteById(id);
    }


    public Optional<EmailConfirmation> findEmailConfirmationByToken(String token) {

        return emailConfirmationRepository.findByConfirmationToken(token);
    }
}

package com.hask.hasktask.controller;

import com.hask.hasktask.event.AccountProducer;
import com.hask.hasktask.model.AuthenticateRequest;
import com.hask.hasktask.model.JWTResponse;
import com.hask.hasktask.model.RegisterRequest;
import com.hask.hasktask.model.VerificationDetails;
import com.hask.hasktask.service.AuthenticationService;
//import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

// For the Purpose of SpringDoc OpenAPI Swagger-UI
//@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Management")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AccountProducer accountProducer;

    private int counter;

    @GetMapping
    public String warnsScam() {
        counter++;
        return String.format("<h1><strong style='color:red;'>STOP!:</strong><br>This is a browser feature intended for developers. " + counter + "</2>");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest httpRequest) {

        VerificationDetails details = authenticationService.register(registerRequest, httpRequest);

        // Trigger Kafka event for account creation
        accountProducer.sendAccountCreatedEvent(details.token(), details.otp(), details.toEmail());

        return ResponseEntity.ok().build();
    }

    /**
     * <a href="http://hostname/api/v1/auth/account/confirm_email?token=eyJhbGciOiJ">...</a>
     */
    @GetMapping("/account/confirm_email")
    public void confirmEmail(@RequestParam String token) {
        authenticationService.confirmAccountViaEmail(token);
    }

    /**
     * <a href="http://hostname/api/v1/auth/account/confirm_otp?otp=465758">...</a>
     */
    @GetMapping("/account/confirm_otp")
    public ResponseEntity<JWTResponse> confirmOTP(@RequestParam String otp, @RequestParam(required = false) String email) {
        if (StringUtils.hasText(otp)) {
            if (StringUtils.hasText(email)) {
                return ResponseEntity.ok(authenticationService.verifyResetPasswordOTP(otp, email));
            } else {
                authenticationService.confirmOTP(otp);
                return ResponseEntity.ok().build(); // Or appropriate response for successful OTP confirmation without email
            }
        }
        return ResponseEntity.badRequest().body(null); // Handle case where OTP is not provided
    }

    /*
     * /api/v1/auth/resend/confirm_email?param=admin@gmail.com */
    @PostMapping("/resend/confirm_email")
    public ResponseEntity<String> resendConfirmMail(@RequestParam String email) {
        VerificationDetails details = authenticationService.resendConfirmationMail(email);

        // Trigger Kafka event for account verification Email
        accountProducer.sendVerifyAccountEvent(details.token(), details.otp(), details.toEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> auth(@Valid @RequestBody AuthenticateRequest authenticateRequest, HttpServletRequest httpRequest) {

        return ResponseEntity.ok(authenticationService.authenticate(authenticateRequest, httpRequest));
    }

    /*
     * REFRESH_TOKEN is saved in the Database for reference CALL.
     * So when ACCESS_TOKEN (JWT_Token) expires,
     * REFRESH_TOKEN is used to get a new ACCESS_TOKEN.
     * POST-REQUEST:: using REFRESH_TOKEN as Authorization 'Bearer Token'
     * Obtain a NEW ACCESS_TOKEN*/
    @PostMapping("/refresh/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Client/User IP Utils.getClientIp(httpRequest);

        authenticationService.refreshToken(request, response);
    }

    /*
     * @Param: user email/ip/phoneNumber
     * /api/v1/auth/user_exist?param=192.168.8.1
     * /api/v1/auth/user_exist?param=233241050915
     * /api/v1/auth/user_exist?param=admin@gmail.com
     * */
    @PostMapping("/user_exist")
    public ResponseEntity<Boolean> userExist(@RequestParam String param) {

        return ResponseEntity.ok(authenticationService.userExists(param));
    }

    /*
     * /api/v1/auth/forgot_password?email=admin@gmail.com */
    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        VerificationDetails details = authenticationService.forgotPassword(email);

        // Trigger Kafka event for forgot password creation
        accountProducer.sendForgotPasswordEvent(details.token(), details.otp(), details.toEmail());
        return ResponseEntity.ok().build();
    }
}

package com.hask.hasktask.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${haskTask.app.hostName}")
    private String hostName;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    protected void sendMessage(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public void sendMail(String token, String otp, String toEmail) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setFrom("<MAIL>Your Hask Account");
        mailMessage.setSubject("Hask OTP: Mail Confirmation Link!");
        mailMessage.setText(
                "This OTP or link will expire in 10 minutes.\n\n"
                        + "Your OTP code is: " + otp + "\n"
                        + "Alternatively, you can use the following link to activate your account:\n"
                        + hostName + "/register/confirm_email?token=" + token
        );

        sendMessage(mailMessage);
    }

    public void tokenNotification(String toEmail, String type) {
        String domain = "Assign API";
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        String user = toEmail.substring(0, toEmail.indexOf("@"));

        mailMessage.setTo(toEmail);
        mailMessage.setFrom("<MAIL>" + domain);
        mailMessage.setSubject("[" + domain + "]: " + type + " Token Created");
        mailMessage.setText(
                "Hello " + user + "\n\n"
                        + "A new " + type + " Token was just created now for your account " + user + "\n"
                        + "If you didn't create this " + type + " Token, please revoke it by creating a new one.\n\n"
                        + "Thank You\n"
                        + "The Assign Team"
        );

        sendMessage(mailMessage);
        /*"Hello " + user + "\n\n"
                        + "A new " + type + " Token was just created now for your account " + user + "\n"
                        + "If you didn't create this " + type + " Token, please revoke it by creating a new one.\n\n"
                        + "Thank You\n"
                        + "The Assign Team"*/
    }
}

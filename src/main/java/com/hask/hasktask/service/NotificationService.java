package com.hask.hasktask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

///  CONTINUE FROM HERE:

@Service
@RequiredArgsConstructor
public class NotificationService {
    @Value("${haskTask.app.baseUrl}")
    private String baseUrl;
    @Value("${haskTask.app.apiEndpoint}")
    private String apiEndpoint;

    private final EmailService emailService;

    public void sendTaskNotification(String toEmail, String title, LocalDateTime dueDate) {
        String subject = "📌 New Task Created: " + title;
        String body = "Hello,\n\nYou have a new task: " + title +
                "\nDue Date: " + dueDate +
                "\n\nStay productive!\nHask Task Planner"
                + "\n\nClick the link below:\n\n" + baseUrl;

        // Send email
        emailService.sendMail(toEmail, subject, body);

        System.out.println("📩 [Email Sent] Task Notification for: " + title);
    }

    public void sendEventNotification(String toEmail, String title, LocalDateTime dueDate) {
        String subject = "📌 New Event Created: " + title;
        String body = "Hello,\n\nYou have a new event: " + title +
                "\nDue Date: " + dueDate +
                "\n\nStay productive!\nHask Task Planner"
                + "\n\nClick the link below:\n\n" + baseUrl;

        // Send email
        emailService.sendMail(toEmail, subject, body);

        System.out.println("📩 [Email Sent] Event Notification for: " + title);
    }

    public void sendVerifyAccountNotification(String token, String otp, String toEmail) {
        var verificationUrl = baseUrl+apiEndpoint + "/account/confirm_email?token=" + token;
        var subject = "📩 Hask Task Planner: Verify Your Account!";
        var body = "Welcome!\n\n"
                + "Your OTP code is: " + otp + "\n"
                + "Alternatively, Click the link below to verify your account:\n\t"
                + verificationUrl + "\n\nThis OTP or link expires in 10 minutes.";

        // Send email
        emailService.sendMail(toEmail, subject, body);
        // Trigger Kafka event for event creation
        // accountProducer.sendEventCreatedEvent(user.getId(), user.getEmail());

        System.out.println("📩 [Verification Email Sent] to: " + toEmail);
    }

    /* *public void sendTimerNotification(String toEmail, String title, String status) {
        String subject = "📌 New Timer Created: " + title;
        String body = "Hello,\n\nYou have a new timer: " + title +
                "\nTimer Completed: " + status +
                "\n\nStay productive!\nHask Task Planner"
                + "\n\nClick the link below:\n\n" + baseUrl;

        // Send email
        emailService.sendMail(toEmail, subject, body);

        System.out.println("📩 [Email Sent] Task Notification for: " + title);
    }*/
}

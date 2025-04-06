package com.hask.hasktask.listener;

import com.hask.hasktask.model.VerificationDetails;
import com.hask.hasktask.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka  // Enable Kafka listener in Spring Boot
@Component    // Register KafkaConsumer as a Spring bean
public class AccountConsumer {

    private final NotificationService notificationService;

    public AccountConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Kafka listener for account events (e.g., ACC_CREATED, ACC_VERIFICATION, ACC_FORGOT)
    @KafkaListener(topics = "${haskTask.app.topics.accountTopic}", groupId = "account-consumer-group")
    public void listenAccountEvents(ConsumerRecord<String, String> record) {
        String message = record.value();
        processAccountMessage(message);
    }

    // Generalized method to process Account messages
    private void processAccountMessage(String msg) {
        VerificationDetails accountDetails = extractAccountDetails(msg);

        if (accountDetails != null) {
            switch (accountDetails.eventType()) {
                case "ACC_CREATED":
                    handleAccountCreated(accountDetails);
                    break;
                case "ACC_VERIFICATION":
                    handleResendVerification(accountDetails);
                    break;
                case "ACC_FORGOT":
                    handleForgotPassword(accountDetails);
                    break;
                default:
                    System.out.println("Unknown acc type");
            }
        }
    }

    private String getPart(String[] parts, int index) {
        return parts[index].split("=")[1];
    }

    private VerificationDetails extractAccountDetails(String msg) {
        String[] parts = msg.split(",");
        if (parts.length < 2) return null; // Invalid message format

        String email = parts[0].split("=")[1];
        String token = getPart(parts, 1);
        String otp = getPart(parts, 2);

        String eventType = extractTaskType(msg);
        return new VerificationDetails(token, otp, email, eventType);
    }

    private String extractTaskType(String msg) {
        if (msg.contains("ACC_CREATED")) return "ACC_CREATED";
        if (msg.contains("ACC_VERIFICATION")) return "ACC_VERIFICATION";
        if (msg.contains("ACC_FORGOT")) return "ACC_FORGOT";
        return "UNKNOWN";
    }

    private void handleAccountCreated(VerificationDetails details) {
        notify(details);
        System.out.println("Account Created Event ID=" + details.toEmail() + ", OTP=" + details.otp() + ", OTP=" + details.token());
    }

    private void handleResendVerification(VerificationDetails details) {
        notify(details);
        System.out.println("Resend Verification Event ID=" + details.toEmail() + ", OTP=" + details.otp() + ", OTP=" + details.token());
    }

    private void handleForgotPassword(VerificationDetails details) {
        notify(details);
        System.out.println("Forgot Password Event ID=" + details.toEmail() + ", OTP=" + details.otp() + ", OTP=" + details.token());
    }


    private void notify(VerificationDetails details) {
        notificationService.sendVerifyAccountNotification(
                details.token(),
                details.otp(),
                details.toEmail()
        );
    }

}


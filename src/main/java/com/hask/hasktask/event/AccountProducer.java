package com.hask.hasktask.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountProducer {

    final private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${haskTask.app.topics.accountTopic}")
    private String accountTopic;  // Kafka topic for account-related events

    public AccountProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // GENERIC METHOD TO SEND EVENTS TO KAFKA TOPICS
    private void sendEvent(String topic, String eventType, String toEmail, String token, String otp) {
        String message = buildAccountMessage(eventType, token, otp, toEmail);
        kafkaTemplate.send(topic, message);
        System.out.println("Sent Kafka message: " + message);
    }

    private static String buildAccountMessage(String eventType, String token, String otp, String toEmail) {
        return eventType + ": id=" + toEmail + ", token=" + token + ", otp=" + otp;
    }

    // SEND ACCOUNT-RELATED EVENTS
    public void sendAccountCreatedEvent(String token, String otp, String toEmail) {
        sendEvent(accountTopic, "ACC_CREATED", toEmail, token, otp);
    }

    public void sendVerifyAccountEvent(String token, String otp, String toEmail) {
        sendEvent(accountTopic, "ACC_VERIFICATION", toEmail, token, otp);
    }

    public void sendForgotPasswordEvent(String token, String otp, String toEmail) {
        sendEvent(accountTopic, "ACC_FORGOT", toEmail, token, otp);
        System.out.println("Steve-Account-success Producer ID " + toEmail);
    }

}


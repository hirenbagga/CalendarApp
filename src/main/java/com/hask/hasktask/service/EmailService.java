package com.hask.hasktask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    protected void sendMessage(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public void sendMail(String to, String subj, String msg) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setFrom("<MAIL>Hask Task");
        mailMessage.setSubject(subj);
        mailMessage.setText(msg);

        sendMessage(mailMessage);
    }

}

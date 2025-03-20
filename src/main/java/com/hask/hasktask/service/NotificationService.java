package com.hask.hasktask.service;

import com.hask.hasktask.model.Notification;
import com.hask.hasktask.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    final private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElse(null);
    }
}

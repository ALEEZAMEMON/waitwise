package com.waitwise.backend.service;

import com.waitwise.backend.dto.notification.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void createNotification(Long userId, String message);

    List<NotificationResponse> getUserNotifications(Long userId);

    NotificationResponse markAsRead(Long notificationId);
}
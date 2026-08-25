package com.waitwise.backend.service;

import com.waitwise.backend.dto.notification.NotificationResponse;
import com.waitwise.backend.entity.Notification;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.NotificationRepository;
import com.waitwise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    // =========================
    // CREATE NOTIFICATION
    // =========================

    @Override
    public void createNotification(
            Long userId,
            String message) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }


    // =========================
    // GET MY NOTIFICATIONS
    // =========================

    @Override
    public List<NotificationResponse> getMyNotifications() {

        User user = getCurrentUser();

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================
    // MARK AS READ
    // =========================

    @Override
    public NotificationResponse markAsRead(
            Long notificationId) {

        User user = getCurrentUser();

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                ));

        /*
         * Make sure the notification belongs
         * to the currently logged-in user.
         */
        if (!notification.getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to access this notification"
            );
        }

        notification.setIsRead(true);

        notification =
                notificationRepository.save(notification);

        return mapToResponse(notification);
    }


    // =========================
    // CURRENT USER
    // =========================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }




    private NotificationResponse mapToResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
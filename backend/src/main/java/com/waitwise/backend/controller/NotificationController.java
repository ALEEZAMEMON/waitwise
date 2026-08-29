package com.waitwise.backend.controller;

import com.waitwise.backend.dto.notification.NotificationResponse;
import com.waitwise.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // =========================
    // GET MY NOTIFICATIONS
    // =========================

    @GetMapping
    public List<NotificationResponse> getMyNotifications() {

        return notificationService.getMyNotifications();
    }

    // =========================
    // MARK AS READ
    // =========================

    @PutMapping("/{id}/read")
    public NotificationResponse markAsRead(
            @PathVariable Long id) {

        return notificationService.markAsRead(id);
    }
}
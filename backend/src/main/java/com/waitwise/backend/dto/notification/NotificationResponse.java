package com.waitwise.backend.dto.notification;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long id;

    private String message;

    private Boolean isRead;

    private LocalDateTime createdAt;
}
package com.waitwise.backend.repository;

import com.waitwise.backend.entity.Notification;
import com.waitwise.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsRead(User user, Boolean isRead);
}
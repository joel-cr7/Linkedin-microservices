package com.linkedin.notificationservice.repository;

import com.linkedin.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

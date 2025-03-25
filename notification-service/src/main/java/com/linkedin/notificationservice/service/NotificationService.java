package com.linkedin.notificationservice.service;


import com.linkedin.notificationservice.entity.Notification;
import com.linkedin.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void addNotification(Notification notification){
        log.info("Adding notification to DB, message: {}", notification.getMessage());
        notificationRepository.save(notification);
    }

}

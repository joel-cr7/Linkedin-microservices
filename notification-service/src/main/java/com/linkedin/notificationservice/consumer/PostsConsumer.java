package com.linkedin.notificationservice.consumer;


import com.linkedin.notificationservice.entity.Notification;
import com.linkedin.notificationservice.service.NotificationService;
import com.linkedin.postservice.event.PostCreatedEvent;
import com.linkedin.postservice.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "post_created_topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("Received PostCreated notification: {}", postCreatedEvent);

        // save notification to DB
        String message = String.format("Your connection with id: %d has created this post: %s",
                postCreatedEvent.getOwnerUserId(), postCreatedEvent.getContent());

        Notification notification = Notification.builder()
                .message(message)
                .userId(postCreatedEvent.getUserId())
                .build();

        notificationService.addNotification(notification);
    }

    @KafkaListener(topics = "post_liked_topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        log.info("Received PostLiked notification: {}", postLikedEvent);

        // save notification to DB (we can send actual email/push notification here)
        String message = String.format("User with id: %d has liked your post with id: %d",
                postLikedEvent.getLikedByUserId(), postLikedEvent.getPostId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(postLikedEvent.getOwnerUserId())
                .build();

        notificationService.addNotification(notification);
    }

}

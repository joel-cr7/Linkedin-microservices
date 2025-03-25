package com.linkedin.notificationservice.consumer;


import com.linkedin.connectionservice.event.ConnectionEvent;
import com.linkedin.notificationservice.entity.Notification;
import com.linkedin.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "connection_request_topic")
    public void handleConnectionRequest(ConnectionEvent connectionEvent){
        log.info("Received connection request notification: {}", connectionEvent);

        // save notification to DB
        String message = String.format("You received a connection request from userid: %d",
                connectionEvent.getSenderUserId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(connectionEvent.getReceiverUserId())
                .build();

        notificationService.addNotification(notification);
    }


    @KafkaListener(topics = "connection_accepted_topic")
    public void handleConnectionAccepted(ConnectionEvent connectionEvent){
        log.info("Received connection accepted notification: {}", connectionEvent);

        // save notification to DB
        String message = String.format("Your connection request has been accepted by userid: %d",
                connectionEvent.getReceiverUserId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(connectionEvent.getSenderUserId())
                .build();

        notificationService.addNotification(notification);
    }


    @KafkaListener(topics = "connection_rejected_topic")
    public void handleConnectionRejected(ConnectionEvent connectionEvent){
        log.info("Received connection rejected notification: {}", connectionEvent);

        // save notification to DB
        String message = String.format("Your connection request has been rejected by userid: %d",
                connectionEvent.getReceiverUserId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(connectionEvent.getSenderUserId())
                .build();

        notificationService.addNotification(notification);
    }

}

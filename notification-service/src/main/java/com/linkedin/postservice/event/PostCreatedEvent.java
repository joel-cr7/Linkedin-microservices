package com.linkedin.postservice.event;


import lombok.Data;

@Data
public class PostCreatedEvent {
    private Long ownerUserId;
    private Long postId;
    private Long userId;        // to whom we want to send the notification
    private String content;
}

package com.linkedin.postservice.event;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {
    private Long ownerUserId;
    private Long postId;
    private Long userId;        // to whom we want to send the notification
    private String content;
}

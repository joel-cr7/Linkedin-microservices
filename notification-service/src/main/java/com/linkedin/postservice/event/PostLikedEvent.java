package com.linkedin.postservice.event;

import lombok.Data;

@Data
public class PostLikedEvent {
    private Long postId;
    private Long ownerUserId;
    private Long likedByUserId;
}

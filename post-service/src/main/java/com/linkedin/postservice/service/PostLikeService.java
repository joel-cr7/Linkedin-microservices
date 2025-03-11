package com.linkedin.postservice.service;


public interface PostLikeService {
    void likePost(Long postId);

    void unlikePost(Long postId);
}

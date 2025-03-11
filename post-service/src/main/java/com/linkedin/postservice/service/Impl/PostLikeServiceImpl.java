package com.linkedin.postservice.service.Impl;


import com.linkedin.postservice.entity.Post;
import com.linkedin.postservice.entity.PostLike;
import com.linkedin.postservice.exception.BadRequestException;
import com.linkedin.postservice.exception.ResourceNotFoundException;
import com.linkedin.postservice.repository.PostLikeRepository;
import com.linkedin.postservice.repository.PostRepository;
import com.linkedin.postservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public void likePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID: {} is liking post with ID: {}", userId, postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        boolean hasAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(hasAlreadyLiked) throw new BadRequestException("You cannot like the post again");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);

        // TODO: send notification to the owner of the post

    }


    @Transactional
    @Override
    public void unlikePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID: {} is un-liking post with ID: {}", userId, postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        boolean hasAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!hasAlreadyLiked) throw new BadRequestException("You cannot unlike the post that you have not liked");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}

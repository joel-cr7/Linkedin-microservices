package com.linkedin.postservice.service.Impl;


import com.linkedin.postservice.dto.PostCreateRequestDTO;
import com.linkedin.postservice.dto.PostDTO;
import com.linkedin.postservice.entity.Post;
import com.linkedin.postservice.exception.ResourceNotFoundException;
import com.linkedin.postservice.repository.PostRepository;
import com.linkedin.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostCreateRequestDTO postCreateRequestDTO, Long userId) {
        log.info("Creating post for user with ID: {}", userId);
        Post post = modelMapper.map(postCreateRequestDTO, Post.class);
        post.setUserId(userId);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO getPostById(Long postId) {
        log.info("Getting post for post with ID: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post Not found with ID: " + postId));

        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<PostDTO> getPostsByUserid(Long userId) {
        log.info("Getting all the posts of user with ID: {}", userId);
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .toList();
    }

}

package com.linkedin.postservice.service;


import com.linkedin.postservice.dto.PostCreateRequestDTO;
import com.linkedin.postservice.dto.PostDTO;

import java.util.List;


public interface PostService {
    PostDTO createPost(PostCreateRequestDTO postCreateRequestDTO, Long userId);

    PostDTO getPostById(Long postId);

    List<PostDTO> getPostsByUserid(Long userId);
}

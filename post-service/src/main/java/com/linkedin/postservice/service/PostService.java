package com.linkedin.postservice.service;


import com.linkedin.postservice.dto.PostCreateRequestDTO;
import com.linkedin.postservice.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface PostService {
    PostDTO createPost(PostCreateRequestDTO postCreateRequestDTO, MultipartFile file);

    PostDTO getPostById(Long postId);

    List<PostDTO> getPostsByUserid(Long userId);
}

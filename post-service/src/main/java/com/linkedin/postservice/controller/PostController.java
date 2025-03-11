package com.linkedin.postservice.controller;


import com.linkedin.postservice.dto.PostCreateRequestDTO;
import com.linkedin.postservice.dto.PostDTO;
import com.linkedin.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostCreateRequestDTO postCreateRequestDTO) {
        PostDTO postDTO = postService.createPost(postCreateRequestDTO, 1L);
        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPostById(postId);
        return ResponseEntity.ok(postDTO);
    }


    @GetMapping("/user/{userId}/allPosts")
    public ResponseEntity<List<PostDTO>> getAllPostsByUserId(@PathVariable Long userId){
        List<PostDTO> postsDTO = postService.getPostsByUserid(userId);
        return ResponseEntity.ok(postsDTO);
    }

}

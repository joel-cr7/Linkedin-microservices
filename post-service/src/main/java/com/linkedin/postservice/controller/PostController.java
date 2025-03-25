package com.linkedin.postservice.controller;


import com.linkedin.postservice.auth.AuthContextHolder;
import com.linkedin.postservice.dto.PostCreateRequestDTO;
import com.linkedin.postservice.dto.PostDTO;
import com.linkedin.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> createPost(@RequestPart("post") PostCreateRequestDTO postCreateRequestDTO,
                                              @RequestPart("file") MultipartFile file) {
        PostDTO postDTO = postService.createPost(postCreateRequestDTO, file);
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

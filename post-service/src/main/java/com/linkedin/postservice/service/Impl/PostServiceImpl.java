package com.linkedin.postservice.service.Impl;


import com.linkedin.postservice.auth.AuthContextHolder;
import com.linkedin.postservice.client.ConnectionsServiceClient;
import com.linkedin.postservice.client.UploaderServiceClient;
import com.linkedin.postservice.dto.PersonDTO;
import com.linkedin.postservice.dto.PostCreateRequestDTO;
import com.linkedin.postservice.dto.PostDTO;
import com.linkedin.postservice.entity.Post;
import com.linkedin.postservice.event.PostCreatedEvent;
import com.linkedin.postservice.exception.ResourceNotFoundException;
import com.linkedin.postservice.repository.PostRepository;
import com.linkedin.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ConnectionsServiceClient connectionsServiceClient;
    private final UploaderServiceClient uploaderServiceClient;
    private final KafkaTemplate<Long, PostCreatedEvent> postCreatedKafkaTemplate;
    private final ModelMapper modelMapper;


    @Override
    public PostDTO createPost(PostCreateRequestDTO postCreateRequestDTO, MultipartFile file) {
        Long userId = AuthContextHolder.getCurrentUserId();
        log.info("Creating post for user with ID: {}", userId);

        ResponseEntity<String> imageUrl = uploaderServiceClient.uploadFile(file);

        Post post = modelMapper.map(postCreateRequestDTO, Post.class);
        post.setUserId(userId);
        post.setImageUrl(imageUrl.getBody());
        post = postRepository.save(post);

        // send notification using kafka to the users who are first-degree connections
        List<PersonDTO> personDTOList = connectionsServiceClient.getFirstDegreeConnections(userId);

        for(PersonDTO person: personDTOList){
            PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .ownerUserId(userId)
                    .userId(person.getUserId())
                    .build();
            postCreatedKafkaTemplate.send("post_created_topic", postCreatedEvent);
        }

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

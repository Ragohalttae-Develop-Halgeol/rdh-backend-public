package com.sfermions.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sfermions.controller.AuthController;
import com.sfermions.dto.post.AddPostRequest;
import com.sfermions.dto.post.UpdatePostRequest;
import com.sfermions.entity.Post;
import com.sfermions.entity.User;
import com.sfermions.repository.PostRepository;
import com.sfermions.repository.UserRepository;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3UploaderService s3UploaderService;
    // Create a new post

    public Post createPost(AddPostRequest addPostRequest) {
        // Find the user who is creating the post
        User user = userRepository.findById(addPostRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        logger.info(addPostRequest.toString());

        // Build the post entity
        Post post = Post.builder()
                .preview(addPostRequest.getPreview())
                .title(addPostRequest.getTitle())
                .content(addPostRequest.getContent())
                .tag(addPostRequest.getTag())
                .user(user)
                .build();
    
        // Save the post in the repository to generate the ID
        Post savedPost = postRepository.save(post);
    
        // Check if an image is present
        if (addPostRequest.getImage() != null && !addPostRequest.getImage().isEmpty()) {
            try {
                // Use the saved post ID for the image upload
                String imageUrl = s3UploaderService.upload(addPostRequest.getImage(), "Post/" + savedPost.getId() + "/image");
                savedPost.setImage(imageUrl);
    
                // Set the image URL to the saved post and update it
                postRepository.save(savedPost); // Update the post with the image URL
            } catch (IOException e) {
                // Handle the exception properly, maybe log it or throw a custom exception
                e.printStackTrace();
                throw new RuntimeException("Failed to upload image", e); // Optionally, throw a custom exception
            }
        }
    
        // Return the saved post with the image URL
        return savedPost;
    }

    // Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostByUserId(Long id) {
        return postRepository.findByUserId(id);
    }

    // Get a post by its ID
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // Update an existing post
    @Transactional
    public Post updatePost(Long id, UpdatePostRequest updatePostRequest) {
        // Find the existing post
        Post existingPost = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.update(updatePostRequest.getPreview(), updatePostRequest.getTitle(), updatePostRequest.getContent());

        return existingPost;
    }
}
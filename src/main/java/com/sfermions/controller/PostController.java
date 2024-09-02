package com.sfermions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sfermions.dto.api.ApiResponse;
import com.sfermions.dto.post.AddPostRequest;
import com.sfermions.dto.post.UpdatePostRequest;
import com.sfermions.entity.Post;
import com.sfermions.entity.User;
import com.sfermions.entity.enums.Tag;
import com.sfermions.service.ContractService;
import com.sfermions.service.PostService;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final PostService postService;
    private final ContractService contractService;

    // post 생성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(
                @RequestParam("preview") String preview,
                @RequestParam("title") String title,
                @RequestParam("content") String content,
                @RequestParam("userId") Long userId,
                @RequestParam("tag") Tag tag,
                @RequestParam("image") MultipartFile image
                ) {
        // AddPostRequest DTO에서 image를 설정
        AddPostRequest addPostRequest = new AddPostRequest();
        addPostRequest.setPreview(preview);
        addPostRequest.setTitle(title);
        addPostRequest.setContent(content);
        addPostRequest.setUserId(userId);
        addPostRequest.setTag(tag);
        addPostRequest.setImage(image);

        Post createdPost = postService.createPost(addPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "GOOD", createdPost));
    }

    // 전체 post 조회
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "GOOD", posts));
    }

    // id 값을 가지고 post 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getPostById(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "GOOD", post));
    }

    // id 값을 가지고 post 조회
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<?>> getPostByUserId(@PathVariable("id") Long id) {
        List<Post> posts = postService.getPostByUserId(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "GOOD", posts));
    }
    
    @GetMapping("/{id}/users")
    public ResponseEntity<ApiResponse<?>> getUsersByPostId(@PathVariable("id") Long id) {
        // 해당 postId로 Users 가져오기
        List<User> users = contractService.getUsersByPostId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "GOOD", users));
    }

    // id 값을 가지고 post 업데이트 
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updatePost(@PathVariable("id") Long id, @RequestBody UpdatePostRequest updatePostRequest) {
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "GOOD", updatedPost));
    }
}
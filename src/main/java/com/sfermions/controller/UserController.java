package com.sfermions.controller;

import java.util.List;

import com.sfermions.dto.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sfermions.dto.user.AddUserRequest;
import com.sfermions.dto.user.UpdateUserRequest;
import com.sfermions.dto.user.UserResponse;
import com.sfermions.entity.User;
import com.sfermions.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user") // 엔드포인트 이름 수정
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 모든 유저 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAllUsers() {
        List<UserResponse> users = userService.findAll()
                .stream()
                .map(UserResponse::new) // User 엔티티를 UserResponse DTO로 변환
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "모든 유저 조회 성공", users));
    }

    //  자신의 정보 조회
    // @GetMapping("/me")
    // public ResponseEntity<ApiResponse> getCurrentUSer() {
    //     User user = userService.getCurrentUser();
    //     return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "현재 사용자 정보 조회 성공", user));
    // }

    // 특정 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> findUserById(@PathVariable("id") long id) {
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "유저 조회 성공", user));
    }

    // 유저 생성
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody AddUserRequest request) {
        User savedUser = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "유저 생성 성공", new UserResponse(savedUser)));
    }

    // 유저 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable("id") long id, @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "유저 업데이트 성공", new UserResponse(updatedUser)));
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "유저 삭제 성공", null));
    }

    // 프로필에 글+사진 업로드
    @PostMapping("/main/{id}")
    public ResponseEntity<ApiResponse<String>> uploadProfile(@RequestParam("content") String content, @RequestParam("file") MultipartFile file, @PathVariable("id") long id) {
        userService.uploadProfileContent(id, content, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "업로드에 성공하였습니다.", "업로드에 성공하였습니다."));
    }
}

package com.sfermions.controller;

import com.sfermions.dto.api.ApiResponse;
import com.sfermions.dto.user.AddUserRequest;
import com.sfermions.dto.user.LoginResponse;
import com.sfermions.entity.User;
import com.sfermions.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map; // 추가된 부분: Map을 사용하기 위해 import 구문 추가

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<?>> getTest() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Api /api/auth/test OK", null));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(@RequestBody AddUserRequest request) {
        try {
            logger.info("/api/auth/signup Request: " + request);
            User createdUser = authService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "User registered successfully", createdUser));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, e.getMessage(), null)); // 409 Conflict
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody AddUserRequest request) {
        try {
            LoginResponse loginResponse = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", loginResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}






package com.sfermions.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sfermions.dto.api.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    // 권한 테스트 (클라이언트 사용 x)
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<?>> getTest() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Api /api/admin/test OK", null));
    }
}


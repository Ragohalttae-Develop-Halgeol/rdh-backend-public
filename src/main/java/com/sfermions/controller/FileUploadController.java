package com.sfermions.controller;

import com.sfermions.service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    private final S3UploaderService s3UploaderService;

    // 파일 업로드 테스트 (클라이언트 사용 x)
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadedUrl = s3UploaderService.upload(file, "uploads/profile"); // "uploads"는 디렉토리 경로
            return ResponseEntity.ok(uploadedUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }
}
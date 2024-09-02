package com.sfermions.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploaderService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // yml에서 버킷 이름을 가져옴

    private final AmazonS3 amazonS3;


    public String upload(MultipartFile file, String filePath) throws IOException {
        // 파일 확장자 추출
        String originalFileName = file.getOriginalFilename();
        String fileExtension = ""; // 확장자 기본값

        // 파일 확장자 설정
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // S3에 저장할 파일 경로 설정 (userID/Profile 형식)
        String fileName = filePath + fileExtension;
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        
        return amazonS3.getUrl(bucketName, fileName).toString(); // 이미지 URL 반환
    }


}
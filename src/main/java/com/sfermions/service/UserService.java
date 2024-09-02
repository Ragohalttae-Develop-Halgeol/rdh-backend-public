package com.sfermions.service;

import java.awt.color.ICC_ColorSpace;
import java.io.IOException;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sfermions.dto.user.AddUserRequest;
import com.sfermions.dto.user.UpdateUserRequest;
import com.sfermions.entity.User;
import com.sfermions.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3UploaderService s3UploaderService;

    // 유저 저장 메소드
    public User save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .introduction(dto.getIntroduction()) // 자기소개 필드 추가
                .career(dto.getCareer()) // 동아리,윙크 면접 등
                .isDeleted(false) // 초기값 설정
                .build());
    }

    // ID로 유저 조회
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

     //유저 정보 업데이트
    public User updateUser(long userId, UpdateUserRequest userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userDetails.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword())); // 비밀번호 암호화 추가
        user.setIntroduction(userDetails.getIntroduction()); // 자기소개 업데이트
        user.getCareer(userDetails.getCareer()); // 경력 업데이트

        return userRepository.save(user);
    }


    //글, 사진 업로드
    @Transactional
    public void uploadProfileContent(long id, String content, MultipartFile file) {
        User user = userRepository.findById(id)  // 파일 저장 로직 추가 (S3, DB 등)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없어 업로드할 수 없습니다. " + id));

        user.uploadProfileContent(id,content, file);
    }

    // 모든 유저 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }


    // 유저 정보 업데이트 (더티 체킹 사용)
    @Transactional
    public User update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        user.update(request.getIntroduction(), request.getCareer()); // 필드 추가

        return user;
    }

    // 유저 삭제
    @Transactional
    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        userRepository.save(user);

    }

    public User getCurrentUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾지 못했습니다: " + 1L));
    }


    // 프로필 사진 업로드

    public User updateProfile(Long userId, UpdateUserRequest userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        MultipartFile profile = userDetails.getProfile();
        if( profile != null && !profile.isEmpty()) {
            try {
                String filepath = "User/" + userId + "/profile"; // S3에 저장될 파일 경로
                String profileUrl = s3UploaderService.upload(profile, filepath); // S3 업로드 후 URL
                user.setProfileImageUrl(profileUrl); // 프로필 URL을 User 엔티티에 저장
            }
            catch (IOException e) {
                throw new RuntimeException("프로필 사진 업로드 중 오류가 발생했습니다.");
            }

        }
        user.setIntroduction(userDetails.getEmail());
        user.setCareer(userDetails.getCareer());

        return userRepository.save(user);
    }


}

package com.sfermions.service;

import com.sfermions.config.jwt.TokenProvider;
import com.sfermions.controller.AuthController;
import com.sfermions.dto.user.AddUserRequest;
import com.sfermions.dto.user.LoginResponse;
import com.sfermions.entity.RefreshToken;
import com.sfermions.entity.User;
import com.sfermions.repository.RefreshTokenRepository;
import com.sfermions.repository.UserRepository;
import com.sfermions.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    public User signup(AddUserRequest request) {
        // 이메일 중복 검사
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }
        
        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role("ROLE_USER")
            .build();

        return userRepository.save(user);
    }

    // 로그인 시 토큰 발급
    public LoginResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        logger.info("Authentication successful for email: " + email);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser(); // 실제 User 엔티티를 가져옴

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(100));
        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14));
        saveRefreshToken(user, refreshToken);

        return new LoginResponse(
                accessToken,
                refreshToken,
                user
        );
    }

    // RefreshToken 저장
    private void saveRefreshToken(User user, String refreshToken) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(user.getId());

        if (existingToken.isPresent()) {
            existingToken.get().update(refreshToken);
            refreshTokenRepository.save(existingToken.get());
        } else {
            RefreshToken token = new RefreshToken(user.getId(), refreshToken);
            refreshTokenRepository.save(token);
        }
    }

    // AccessToken 갱신
    public String refreshAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or Expired Refresh Token");
        }

        Long userId = tokenProvider.getUserId(refreshToken);

        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUserId(userId);
        if (refreshTokenOpt.isEmpty() || !refreshTokenOpt.get().getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return tokenProvider.generateToken(user, Duration.ofHours(1));
    }

    // 로그아웃 처리
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
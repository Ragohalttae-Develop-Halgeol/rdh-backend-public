package com.sfermions.repository; // 패키지 경로는 실제 위치에 맞게 수정하세요

import com.sfermions.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 사용자 ID를 기반으로 리프레시 토큰을 찾는 메서드
    Optional<RefreshToken> findByUserId(Long userId);

    // 리프레시 토큰 문자열을 기반으로 리프레시 토큰을 찾는 메서드
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByUserId(Long userId);
}
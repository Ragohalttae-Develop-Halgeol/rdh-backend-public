package com.sfermions.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.sfermions.entity.User;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Date;
import java.util.Set;
import java.time.Duration;
import java.util.stream.Collectors;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    
    private final JwtProperties jwtProperties;

    // JWT 토큰 생성 메서드
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 내부 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
                .setIssuer(jwtProperties.getIssuer()) // 발급자 설정
                .setIssuedAt(now) // 발급 시간 설정
                .setExpiration(expiry) // 만료 시간 설정
                .setSubject(user.getEmail()) // 사용자 이메일 설정
                .claim("id", user.getId()) // 사용자 ID 설정
                .claim("role", user.getRole()) // 사용자 role 설정
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명 설정
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey()) // 비밀키로 복호화
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) { // 복호화 실패 시 유효하지 않은 토큰으로 처리
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        
        // role 정보를 claims에서 추출
        String role = claims.get("role", String.class);
        
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority(role) // 역할을 권한으로 설정
        );
    
        return new UsernamePasswordAuthenticationToken(
            new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities
            ),
            token,
            authorities
        );
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // 토큰에서 클레임(Claims)을 추출하는 메서드
    private Claims getClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtProperties.getSecretKey())
                   .parseClaimsJws(token)
                   .getBody();
    }
}
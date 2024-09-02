package com.sfermions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.sfermions.config.jwt.TokenAuthenticationFilter;
import com.sfermions.config.jwt.TokenProvider;
import com.sfermions.security.CustomUserDetailsService;
import com.sfermions.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final TokenProvider tokenProvider; // TokenProvider 주입

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
            .authorizeHttpRequests( // 최신 방식: authorizeHttpRequests 사용
                auth -> auth
                    .requestMatchers("/api/auth/**").permitAll() // 특정 경로는 모두 허용
                    .requestMatchers("/api/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMIN") 
                    .requestMatchers("api/contract/**").hasAnyAuthority("ROLE_USER")
                    .anyRequest().permitAll() // 그 외의 모든 요청은 인증 불필요
            )
            .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 필터 등록
            .formLogin((form) -> form.disable()) // 폼 로그인을 비활성화
            .httpBasic((httpBasic) -> httpBasic.disable()) // 비밀번호를 텍스트로 전송하는 방식, HTTP 기본 인증 비활성화
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*")); // 허용된 출처 설정
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            }))
            .sessionManagement((session) -> session  //세션 유지 기능 비활성화, JWT 방식은 세션을 유지할 필요가 없다. 
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션을 상태 없이 관리

        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService customUserDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(customUserDetailsService) // CustomUserDetailsService 사용
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }
}

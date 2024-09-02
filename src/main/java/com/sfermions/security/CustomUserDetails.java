package com.sfermions.security;

import com.sfermions.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails { // UserDetails 상속받아 인증 객체 사용

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override // 사용자의 패스워드 반환
    public String getPassword() {
        return user.getPassword();
    }

    @Override // 사용자의 식별자를 반환(email을 식별자로 사용)
    public String getUsername() {
        return user.getEmail();
    }

    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true; // true -> 만료되지 않았음
    }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true; // true -> 잠금되지 않았음
    }

    @Override // 패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true; // true -> 만료되지 않았음
    }

    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        return true; // true -> 사용 가능
    }

    public User getUser() {
        return user;
    }
}
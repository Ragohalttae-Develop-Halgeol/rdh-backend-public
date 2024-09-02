package com.sfermions.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile", nullable = true)
    private String profile; // S3 URL

    @Column(name = "role", nullable = true)
    private String role; // ROLE_USER, ROLE_ADMIN...

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "career", nullable = true)
    private String career;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public User(String name, String email, String password, String profile, String role, String introduction, String career, Boolean isDeleted) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.role = role;
        this.introduction = introduction;
        this.career = career;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    public void update(String name, String introduction, String career) {

    }

    public void setEmail(String email) {
    }

    public void setPassword(String password) {
    }

    public void getCareer(String career) {
    }

    public void setIntroduction(Object introduction) {
    }

    public void update(Object introduction, Object career) {
    }

    public void uploadProfileContent(long id, String content, MultipartFile file) {

    }

    public void setProfileImageUrl(String profileUrl) {
    }

    public void setCareer(String career) {

    }
}

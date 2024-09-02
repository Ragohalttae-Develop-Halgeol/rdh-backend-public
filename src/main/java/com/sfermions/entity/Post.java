package com.sfermions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sfermions.entity.enums.Tag;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // @Column(name = "is_shared")
    // private Boolean isShared = true; // 만들면 자동 공유
    
    @Column(name = "preview", nullable = false)
    private String preview;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image", nullable = true)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    private Tag tag;

    @Builder
    public Post(String preview, String title, String content, User user, Tag tag, Boolean isDeleted, String image) {
        this.preview = preview;
        this.title = title;
        this.content = content;
        this.user = user;
        this.tag = tag;
        this.isDeleted = isDeleted != null ? isDeleted : false; // 기본값 false로 설정
        this.image = image;
        // this.isShared = isShared != null ? isShared : true; // 기본값 true로 설정
    }

    public void update(String preview, String title, String content) {
        this.preview = preview;
        this.title = title;
        this.content = content;
    }
}
package com.sfermions.dto.post;

import org.springframework.web.multipart.MultipartFile;

import com.sfermions.entity.enums.Tag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddPostRequest {
    private String preview;
    private String title;
    private String content;
    private Long userId;
    private Tag tag;
    private MultipartFile image;
}

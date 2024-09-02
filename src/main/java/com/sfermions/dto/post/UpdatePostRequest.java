package com.sfermions.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostRequest {
    private String preview;
    private String title;
    private String content;
    private Long userId;
    private Long tagId;
    private Long categoryId;
}

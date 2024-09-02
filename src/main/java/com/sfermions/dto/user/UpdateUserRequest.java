package com.sfermions.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String name;
    private String email;
    private String password;
    private String introduction;
    private String career;
    private MultipartFile profile; // 프로필 사진 업로드를 위한 필드 추가
}

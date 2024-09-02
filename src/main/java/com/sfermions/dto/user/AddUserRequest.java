package com.sfermions.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String name;
    private String email;
    private String password;
    private String introduction;
    private String career;
    private String profile;


}

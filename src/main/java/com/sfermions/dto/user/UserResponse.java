package com.sfermions.dto.user;

import com.sfermions.entity.User;

import lombok.Getter;

@Getter
public class UserResponse {
    
    private final String name;

    public UserResponse(User user) {
        this.name = user.getName();
    }
}

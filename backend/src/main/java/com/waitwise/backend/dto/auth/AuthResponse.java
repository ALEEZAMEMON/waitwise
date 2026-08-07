package com.waitwise.backend.dto.auth;

import com.waitwise.backend.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private UserResponse user;

}
package com.waitwise.backend.dto.user;

import com.waitwise.backend.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;

}
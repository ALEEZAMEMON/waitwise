package com.waitwise.backend.dto.business;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessOwnerResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
}
package com.waitwise.backend.dto.business;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessOwnerResponse {

    private Long id;

    private Long userId;

    private Long businessId;

    private String businessName;

    private String fullName;

    private String email;

    private String phoneNumber;
}
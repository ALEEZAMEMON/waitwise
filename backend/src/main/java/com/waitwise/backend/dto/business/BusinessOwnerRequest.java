package com.waitwise.backend.dto.business;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessOwnerRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long businessId;

    private String fullName;

    private String email;

    private String phoneNumber;
}
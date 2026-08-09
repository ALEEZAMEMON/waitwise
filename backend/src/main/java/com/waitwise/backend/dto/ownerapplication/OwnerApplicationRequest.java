package com.waitwise.backend.dto.ownerapplication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerApplicationRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String cnic;

    @NotBlank
    private String businessName;

    @NotBlank
    private String businessType;

    @NotBlank
    private String businessAddress;
}
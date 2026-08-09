package com.waitwise.backend.dto.ownerapplication;

import com.waitwise.backend.enums.OwnerApplicationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerApplicationResponse {

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String cnic;

    private String businessName;

    private String businessType;

    private String businessAddress;

    private OwnerApplicationStatus status;
}
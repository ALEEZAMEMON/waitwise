package com.waitwise.backend.dto;

import com.waitwise.backend.enums.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AppointmentResponse {

    private Long id;
    private Long businessId;
    private String businessName;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
}
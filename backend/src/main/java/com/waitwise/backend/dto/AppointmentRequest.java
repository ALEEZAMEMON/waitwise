package com.waitwise.backend.dto;

import com.waitwise.backend.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequest {

    @NotNull
    private Long businessId;

    @NotNull
    private LocalDateTime appointmentTime;

    private AppointmentStatus status;
}
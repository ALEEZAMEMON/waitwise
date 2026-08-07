package com.waitwise.backend.dto.appointment;

import com.waitwise.backend.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAppointmentStatusRequest {

    @NotNull
    private AppointmentStatus status;
}
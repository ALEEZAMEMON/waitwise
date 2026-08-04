package com.waitwise.backend.dto;

import com.waitwise.backend.enums.QueueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueRequest {

    @NotNull
    private Long appointmentId;

    private QueueStatus status;
}
package com.waitwise.backend.dto.queue;

import com.waitwise.backend.enums.QueueStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueueResponse {

    private Long id;
    private Long appointmentId;
    private Integer queueNumber;
    private QueueStatus status;
}
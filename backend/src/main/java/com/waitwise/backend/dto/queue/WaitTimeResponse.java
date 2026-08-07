package com.waitwise.backend.dto.queue;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WaitTimeResponse {

    private Long queueId;

    private Integer estimatedWaitMinutes;
}
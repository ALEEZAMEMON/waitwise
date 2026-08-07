package com.waitwise.backend.dto.queue;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueuePositionResponse {

    private Long queueId;

    private Integer queueNumber;

    private Integer peopleAhead;

    private Integer estimatedWaitMinutes;
}
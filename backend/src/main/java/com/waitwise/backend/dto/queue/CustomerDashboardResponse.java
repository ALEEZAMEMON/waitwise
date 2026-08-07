package com.waitwise.backend.dto.queue;

import com.waitwise.backend.enums.QueueStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerDashboardResponse {

    private Long appointmentId;

    private Long queueId;

    private String businessName;

    private Integer queueNumber;

    private Integer peopleAhead;

    private Integer estimatedWaitMinutes;

    private QueueStatus queueStatus;
}
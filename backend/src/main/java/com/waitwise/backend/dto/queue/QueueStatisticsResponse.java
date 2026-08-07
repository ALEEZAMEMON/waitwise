package com.waitwise.backend.dto.queue;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueueStatisticsResponse {

    private long totalQueues;

    private long waitingCustomers;

    private long servingCustomers;

    private long completedCustomers;

    private long cancelledCustomers;
}
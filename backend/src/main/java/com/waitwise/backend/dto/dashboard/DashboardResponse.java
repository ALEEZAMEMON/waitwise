package com.waitwise.backend.dto.dashboard;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    private long totalAppointments;

    private long totalQueues;

    private long waitingCustomers;

    private long currentlyServing;

    private long completedCustomers;

    private long cancelledCustomers;
}
package com.waitwise.backend.dto.business;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessDashboardResponse {

    private Long businessId;

    private String businessName;

    private long totalAppointments;

    private long totalQueues;

    private long waitingCustomers;

    private long currentlyServing;

    private long completedCustomers;

    private long cancelledCustomers;
}
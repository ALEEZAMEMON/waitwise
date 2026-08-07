package com.waitwise.backend.service;

import com.waitwise.backend.dto.BusinessDashboardResponse;
import com.waitwise.backend.dto.DashboardResponse;

public interface DashboardService {

    DashboardResponse getDashboard();

    BusinessDashboardResponse getBusinessDashboard(Long businessId);
}
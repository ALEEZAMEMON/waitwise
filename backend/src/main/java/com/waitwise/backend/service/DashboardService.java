package com.waitwise.backend.service;

import com.waitwise.backend.dto.business.BusinessDashboardResponse;
import com.waitwise.backend.dto.dashboard.DashboardResponse;

public interface DashboardService {

    DashboardResponse getDashboard();

    BusinessDashboardResponse getBusinessDashboard(Long businessId);
}
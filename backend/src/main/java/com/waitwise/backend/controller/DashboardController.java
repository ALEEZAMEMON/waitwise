package com.waitwise.backend.controller;

import com.waitwise.backend.dto.BusinessDashboardResponse;
import com.waitwise.backend.dto.DashboardResponse;
import com.waitwise.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard() {

        return dashboardService.getDashboard();
    }

    @GetMapping("/business/{businessId}")
    public BusinessDashboardResponse getBusinessDashboard(
            @PathVariable Long businessId) {

        return dashboardService.getBusinessDashboard(businessId);
    }
}
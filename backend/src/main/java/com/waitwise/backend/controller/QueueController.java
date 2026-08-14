package com.waitwise.backend.controller;

import com.waitwise.backend.dto.queue.CustomerDashboardResponse;
import com.waitwise.backend.dto.queue.QueuePositionResponse;
import com.waitwise.backend.dto.queue.QueueRequest;
import com.waitwise.backend.dto.queue.QueueResponse;
import com.waitwise.backend.dto.queue.QueueStatisticsResponse;
import com.waitwise.backend.dto.queue.WaitTimeResponse;
import com.waitwise.backend.service.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;


    // =========================
    // CUSTOMER
    // =========================

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public QueueResponse createQueue(
            @Valid @RequestBody QueueRequest request) {

        return queueService.createQueue(request);
    }


    @GetMapping("/{queueId}/position")
    @PreAuthorize("hasRole('CUSTOMER')")
    public QueuePositionResponse getQueuePosition(
            @PathVariable Long queueId) {

        return queueService.getQueuePosition(queueId);
    }


    @GetMapping("/{queueId}/wait-time")
    @PreAuthorize("hasRole('CUSTOMER')")
    public WaitTimeResponse getEstimatedWaitTime(
            @PathVariable Long queueId) {

        return queueService.getEstimatedWaitTime(queueId);
    }


    @GetMapping("/dashboard/{appointmentId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public CustomerDashboardResponse getCustomerDashboard(
            @PathVariable Long appointmentId) {

        return queueService.getCustomerDashboard(appointmentId);
    }


    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public QueueResponse cancelQueue(
            @PathVariable Long id) {

        return queueService.cancelQueue(id);
    }


    // =========================
    // BUSINESS OWNER
    // =========================

    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public List<QueueResponse> getBusinessQueue(
            @PathVariable Long businessId) {

        return queueService.getBusinessQueue(businessId);
    }


    @GetMapping("/business/{businessId}/current")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public QueueResponse getCurrentServing(
            @PathVariable Long businessId) {

        return queueService.getCurrentServing(businessId);
    }


    @PutMapping("/business/{businessId}/next")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public QueueResponse callNextCustomer(
            @PathVariable Long businessId) {

        return queueService.callNextCustomer(businessId);
    }


    @PutMapping("/business/{businessId}/complete")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public QueueResponse completeCurrentCustomer(
            @PathVariable Long businessId) {

        return queueService.completeCurrentCustomer(businessId);
    }


    @GetMapping("/statistics")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public QueueStatisticsResponse getQueueStatistics() {

        return queueService.getQueueStatistics();
    }


    // =========================
    // ADMIN
    // =========================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<QueueResponse> getAllQueues() {

        return queueService.getAllQueues();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public QueueResponse getQueueById(
            @PathVariable Long id) {

        return queueService.getQueueById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public QueueResponse updateQueue(
            @PathVariable Long id,
            @Valid @RequestBody QueueRequest request) {

        return queueService.updateQueue(id, request);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteQueue(
            @PathVariable Long id) {

        queueService.deleteQueue(id);

        return "Queue deleted successfully";
    }
}
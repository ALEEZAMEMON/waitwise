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

    // Customer joins a queue
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public QueueResponse createQueue(
            @Valid @RequestBody QueueRequest request) {

        return queueService.createQueue(request);
    }

    // Customer views their queues
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'BUSINESS_OWNER', 'ADMIN')")
    public List<QueueResponse> getAllQueues() {

        return queueService.getAllQueues();
    }

    // View a specific queue
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'BUSINESS_OWNER', 'ADMIN')")
    public QueueResponse getQueueById(
            @PathVariable Long id) {

        return queueService.getQueueById(id);
    }

    // Customer can cancel their queue
    // Business owner can also cancel a queue
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'BUSINESS_OWNER')")
    public QueueResponse cancelQueue(
            @PathVariable Long id) {

        return queueService.cancelQueue(id);
    }

    // Customer checks their queue position
    @GetMapping("/{queueId}/position")
    @PreAuthorize("hasRole('USER')")
    public QueuePositionResponse getQueuePosition(
            @PathVariable Long queueId) {

        return queueService.getQueuePosition(queueId);
    }

    // Customer checks estimated waiting time
    @GetMapping("/{queueId}/wait-time")
    @PreAuthorize("hasRole('USER')")
    public WaitTimeResponse getEstimatedWaitTime(
            @PathVariable Long queueId) {

        return queueService.getEstimatedWaitTime(queueId);
    }

    // Customer dashboard
    @GetMapping("/dashboard/{appointmentId}")
    @PreAuthorize("hasRole('USER')")
    public CustomerDashboardResponse getCustomerDashboard(
            @PathVariable Long appointmentId) {

        return queueService.getCustomerDashboard(appointmentId);
    }

    // =========================
    // BUSINESS OWNER
    // =========================

    // View all queues of a business
    @GetMapping("/business/{businessId}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public List<QueueResponse> getBusinessQueue(
            @PathVariable Long businessId) {

        return queueService.getBusinessQueue(businessId);
    }

    // View currently serving customer
    @GetMapping("/business/{businessId}/current")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public QueueResponse getCurrentServing(
            @PathVariable Long businessId) {

        return queueService.getCurrentServing(businessId);
    }

    // Call next customer
    @PutMapping("/business/{businessId}/next")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public QueueResponse callNextCustomer(
            @PathVariable Long businessId) {

        return queueService.callNextCustomer(businessId);
    }

    // Complete currently serving customer
    @PutMapping("/business/{businessId}/complete")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public QueueResponse completeCurrentCustomer(
            @PathVariable Long businessId) {

        return queueService.completeCurrentCustomer(businessId);
    }

    // =========================
    // ADMIN
    // =========================

    // Update queue
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public QueueResponse updateQueue(
            @PathVariable Long id,
            @Valid @RequestBody QueueRequest request) {

        return queueService.updateQueue(id, request);
    }

    // Delete queue
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteQueue(
            @PathVariable Long id) {

        queueService.deleteQueue(id);

        return "Queue deleted successfully";
    }

    // Queue statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public QueueStatisticsResponse getQueueStatistics() {

        return queueService.getQueueStatistics();
    }
}
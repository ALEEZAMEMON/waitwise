package com.waitwise.backend.controller;

import com.waitwise.backend.dto.QueueRequest;
import com.waitwise.backend.dto.QueueResponse;
import com.waitwise.backend.service.QueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queues")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping
    public QueueResponse createQueue(@Valid @RequestBody QueueRequest request) {

        return queueService.createQueue(request);
    }

    @GetMapping
    public List<QueueResponse> getAllQueues() {

        return queueService.getAllQueues();
    }

    @GetMapping("/{id}")
    public QueueResponse getQueueById(@PathVariable Long id) {

        return queueService.getQueueById(id);
    }

    @PutMapping("/{id}")
    public QueueResponse updateQueue(
            @PathVariable Long id,
            @Valid @RequestBody QueueRequest request) {

        return queueService.updateQueue(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteQueue(@PathVariable Long id) {

        queueService.deleteQueue(id);

        return "Queue deleted successfully";
    }
    @GetMapping("/business/{businessId}")
    public List<QueueResponse> getBusinessQueue(@PathVariable Long businessId) {

        return queueService.getBusinessQueue(businessId);
    }

    @GetMapping("/business/{businessId}/current")
    public QueueResponse getCurrentServing(@PathVariable Long businessId) {

        return queueService.getCurrentServing(businessId);
    }

    @PutMapping("/business/{businessId}/next")
    public QueueResponse callNextCustomer(@PathVariable Long businessId) {

        return queueService.callNextCustomer(businessId);
    }
}
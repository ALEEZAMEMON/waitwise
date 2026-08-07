package com.waitwise.backend.service;

import com.waitwise.backend.dto.queue.QueueRequest;
import com.waitwise.backend.dto.queue.QueueResponse;
import com.waitwise.backend.dto.queue.CustomerDashboardResponse;
import com.waitwise.backend.dto.queue.QueuePositionResponse;
import com.waitwise.backend.dto.queue.QueueStatisticsResponse;
import com.waitwise.backend.dto.queue.WaitTimeResponse;

import java.util.List;

public interface QueueService {

    QueueResponse createQueue(QueueRequest request);

    List<QueueResponse> getAllQueues();

    QueueResponse getQueueById(Long id);

    QueueResponse updateQueue(Long id, QueueRequest request);

    void deleteQueue(Long id);

    List<QueueResponse> getBusinessQueue(Long businessId);

    QueueResponse getCurrentServing(Long businessId);

    QueueResponse callNextCustomer(Long businessId);

    QueueResponse cancelQueue(Long id);
    QueueResponse completeCurrentCustomer(Long businessId);

    QueuePositionResponse getQueuePosition(Long queueId);

    WaitTimeResponse getEstimatedWaitTime(Long queueId);

    CustomerDashboardResponse getCustomerDashboard(Long appointmentId);

    QueueStatisticsResponse getQueueStatistics();
}
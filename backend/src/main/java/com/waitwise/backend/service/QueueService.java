package com.waitwise.backend.service;

import com.waitwise.backend.dto.queue.QueueRequest;
import com.waitwise.backend.dto.queue.QueueResponse;

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
    QueueResponse completeCurrentCustomer(Long businessId);
    QueueResponse cancelQueue(Long id);
}
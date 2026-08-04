package com.waitwise.backend.service;

import com.waitwise.backend.dto.QueueRequest;
import com.waitwise.backend.dto.QueueResponse;

import java.util.List;

public interface QueueService {

    QueueResponse createQueue(QueueRequest request);

    List<QueueResponse> getAllQueues();

    QueueResponse getQueueById(Long id);

    QueueResponse updateQueue(Long id, QueueRequest request);

    void deleteQueue(Long id);
}
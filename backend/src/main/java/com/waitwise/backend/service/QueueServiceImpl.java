package com.waitwise.backend.service;

import com.waitwise.backend.dto.QueueRequest;
import com.waitwise.backend.dto.QueueResponse;
import com.waitwise.backend.entity.Appointment;
import com.waitwise.backend.entity.Queue;
import com.waitwise.backend.repository.AppointmentRepository;
import com.waitwise.backend.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public QueueResponse createQueue(QueueRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Queue lastQueue = queueRepository.findTopByOrderByQueueNumberDesc();

        int nextQueueNumber = (lastQueue == null)
                ? 1
                : lastQueue.getQueueNumber() + 1;

        Queue queue = Queue.builder()
                .appointment(appointment)
                .queueNumber(nextQueueNumber)
                .status(request.getStatus())
                .build();

        queue = queueRepository.save(queue);

        return mapToResponse(queue);
    }

    @Override
    public List<QueueResponse> getAllQueues() {

        return queueRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public QueueResponse getQueueById(Long id) {

        Queue queue = queueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queue not found"));

        return mapToResponse(queue);
    }

    @Override
    public QueueResponse updateQueue(Long id, QueueRequest request) {

        Queue queue = queueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queue not found"));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        queue.setAppointment(appointment);
        queue.setStatus(request.getStatus());

        queue = queueRepository.save(queue);

        return mapToResponse(queue);
    }

    @Override
    public void deleteQueue(Long id) {

        Queue queue = queueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queue not found"));

        queueRepository.delete(queue);
    }

    private QueueResponse mapToResponse(Queue queue) {

        return QueueResponse.builder()
                .id(queue.getId())
                .appointmentId(queue.getAppointment().getId())
                .queueNumber(queue.getQueueNumber())
                .status(queue.getStatus())
                .build();
    }
}
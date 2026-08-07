package com.waitwise.backend.service;

import com.waitwise.backend.dto.queue.QueueRequest;
import com.waitwise.backend.dto.queue.QueueResponse;
import com.waitwise.backend.entity.Appointment;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.Queue;
import com.waitwise.backend.enums.QueueStatus;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.AppointmentRepository;
import com.waitwise.backend.repository.BusinessRepository;
import com.waitwise.backend.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.waitwise.backend.dto.queue.CustomerDashboardResponse;
import com.waitwise.backend.dto.queue.QueuePositionResponse;
import com.waitwise.backend.dto.queue.QueueStatisticsResponse;
import com.waitwise.backend.dto.queue.WaitTimeResponse;
import com.waitwise.backend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;

    @Override
    public QueueResponse createQueue(QueueRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Queue lastQueue = queueRepository.findTopByOrderByQueueNumberDesc();

        int nextQueueNumber = (lastQueue == null)
                ? 1
                : lastQueue.getQueueNumber() + 1;

        Queue queue = Queue.builder()
                .appointment(appointment)
                .queueNumber(nextQueueNumber)
                .status(QueueStatus.WAITING)
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
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        return mapToResponse(queue);
    }

    @Override
    public QueueResponse updateQueue(Long id, QueueRequest request) {

        Queue queue = queueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        queue.setAppointment(appointment);
        queue.setStatus(request.getStatus());

        queue = queueRepository.save(queue);

        return mapToResponse(queue);
    }

    @Override
    public void deleteQueue(Long id) {

        Queue queue = queueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        queueRepository.delete(queue);
    }

    @Override
    public List<QueueResponse> getBusinessQueue(Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        return queueRepository.findByAppointment_BusinessOrderByQueueNumberAsc(business)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public QueueResponse getCurrentServing(Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        Queue queue = queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.SERVING)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No customer is currently being served"));

        return mapToResponse(queue);
    }

    @Override
    public QueueResponse callNextCustomer(Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.SERVING)
                .ifPresent(queue -> {
                    queue.setStatus(QueueStatus.COMPLETED);
                    queueRepository.save(queue);
                });

        Queue nextQueue = queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.WAITING)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No waiting customers"));

        nextQueue.setStatus(QueueStatus.SERVING);

        queueRepository.save(nextQueue);

        return mapToResponse(nextQueue);
    }

    @Override
    public QueueResponse completeCurrentCustomer(Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        Queue currentQueue = queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.SERVING)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No customer is currently being served"));

        currentQueue.setStatus(QueueStatus.COMPLETED);

        queueRepository.save(currentQueue);

        return mapToResponse(currentQueue);
    }
    @Override
    public QueueResponse cancelQueue(Long id) {

        Queue queue = queueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Queue not found"));

        if (queue.getStatus() == QueueStatus.COMPLETED) {
            throw new RuntimeException("Completed queue cannot be cancelled");
        }

        queue.setStatus(QueueStatus.CANCELLED);

        queue = queueRepository.save(queue);

        return mapToResponse(queue);
    }

    @Override
    public QueuePositionResponse getQueuePosition(Long queueId) {

        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Queue not found"));

        Business business = queue.getAppointment().getBusiness();

        List<Queue> waitingQueues = queueRepository
                .findByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.WAITING);

        int peopleAhead = (int) waitingQueues.stream()
                .filter(q -> q.getQueueNumber() < queue.getQueueNumber())
                .count();

        int estimatedWait = peopleAhead * 10;

        return QueuePositionResponse.builder()
                .queueId(queue.getId())
                .queueNumber(queue.getQueueNumber())
                .peopleAhead(peopleAhead)
                .estimatedWaitMinutes(estimatedWait)
                .build();
    }

    @Override
    public WaitTimeResponse getEstimatedWaitTime(Long queueId) {

        QueuePositionResponse position = getQueuePosition(queueId);

        return WaitTimeResponse.builder()
                .queueId(position.getQueueId())
                .estimatedWaitMinutes(position.getEstimatedWaitMinutes())
                .build();
    }
    @Override
    public QueueStatisticsResponse getQueueStatistics() {

        return QueueStatisticsResponse.builder()
                .totalQueues(queueRepository.count())
                .waitingCustomers(queueRepository.countByStatus(QueueStatus.WAITING))
                .servingCustomers(queueRepository.countByStatus(QueueStatus.SERVING))
                .completedCustomers(queueRepository.countByStatus(QueueStatus.COMPLETED))
                .cancelledCustomers(queueRepository.countByStatus(QueueStatus.CANCELLED))
                .build();
    }
    @Override
    public CustomerDashboardResponse getCustomerDashboard(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found"));

        Queue queue = queueRepository.findByAppointment(appointment)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Queue not found"));

        QueuePositionResponse position = getQueuePosition(queue.getId());

        return CustomerDashboardResponse.builder()
                .appointmentId(appointment.getId())
                .queueId(queue.getId())
                .businessName(appointment.getBusiness().getName())
                .queueNumber(queue.getQueueNumber())
                .peopleAhead(position.getPeopleAhead())
                .estimatedWaitMinutes(position.getEstimatedWaitMinutes())
                .queueStatus(queue.getStatus())
                .build();
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
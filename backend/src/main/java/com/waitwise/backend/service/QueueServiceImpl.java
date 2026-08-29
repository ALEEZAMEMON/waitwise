package com.waitwise.backend.service;

import com.waitwise.backend.dto.queue.CustomerDashboardResponse;
import com.waitwise.backend.dto.queue.QueuePositionResponse;
import com.waitwise.backend.dto.queue.QueueRequest;
import com.waitwise.backend.dto.queue.QueueResponse;
import com.waitwise.backend.dto.queue.QueueStatisticsResponse;
import com.waitwise.backend.dto.queue.WaitTimeResponse;
import com.waitwise.backend.entity.Appointment;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.BusinessOwner;
import com.waitwise.backend.entity.Queue;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.enums.QueueStatus;
import com.waitwise.backend.enums.Role;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.AppointmentRepository;
import com.waitwise.backend.repository.BusinessOwnerRepository;
import com.waitwise.backend.repository.BusinessRepository;
import com.waitwise.backend.repository.QueueRepository;
import com.waitwise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final QueueRepository queueRepository;
    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final NotificationService notificationService;


    // =========================
    // CREATE QUEUE
    // =========================

    @Override
    public QueueResponse createQueue(QueueRequest request) {

        User user = getCurrentUser();

        Appointment appointment = appointmentRepository.findById(
                request.getAppointmentId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Appointment not found"));

        // Admin can create a queue for any appointment.
        // Normal users can only join their own appointment.
        if (user.getRole() != Role.ADMIN &&
                !appointment.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to join this appointment's queue"
            );
        }

        // Prevent duplicate queue entries.
        if (queueRepository.findByAppointment(appointment).isPresent()) {

            throw new RuntimeException(
                    "This appointment is already in the queue"
            );
        }

        Queue lastQueue =
                queueRepository.findTopByOrderByQueueNumberDesc();

        int nextQueueNumber = (lastQueue == null)
                ? 1
                : lastQueue.getQueueNumber() + 1;

        Queue queue = Queue.builder()
                .appointment(appointment)
                .queueNumber(nextQueueNumber)
                .status(QueueStatus.WAITING)
                .build();

        queue = queueRepository.save(queue);

        notificationService.createNotification(
                appointment.getUser().getId(),
                "You have successfully joined the queue. Your queue number is "
                        + queue.getQueueNumber() + "."
        );

        return mapToResponse(queue);
    }

    // =========================
    // GET ALL QUEUES
    // =========================

    @Override
    public List<QueueResponse> getAllQueues() {

        User user = getCurrentUser();

        // Only admin can see every queue.
        if (user.getRole() == Role.ADMIN) {

            return queueRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        // Normal users only see their own queues.
        return queueRepository.findAll()
                .stream()
                .filter(queue ->
                        queue.getAppointment()
                                .getUser()
                                .getId()
                                .equals(user.getId()))
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // GET QUEUE BY ID
    // =========================

    @Override
    public QueueResponse getQueueById(Long id) {

        User user = getCurrentUser();

        Queue queue = findQueue(id);

        verifyQueueAccess(queue, user);

        return mapToResponse(queue);
    }

    // =========================
    // UPDATE QUEUE
    // =========================

    @Override
    public QueueResponse updateQueue(
            Long id,
            QueueRequest request) {

        User user = getCurrentUser();

        Queue queue = findQueue(id);

        verifyQueueAccess(queue, user);

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        // Make sure the new appointment also belongs
        // to the same user unless admin is performing it.
        if (user.getRole() != Role.ADMIN &&
                !appointment.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to use this appointment"
            );
        }

        queue.setAppointment(appointment);

        if (request.getStatus() != null) {
            queue.setStatus(request.getStatus());
        }

        queue = queueRepository.save(queue);

        return mapToResponse(queue);
    }

    // =========================
    // DELETE QUEUE
    // =========================

    @Override
    public void deleteQueue(Long id) {

        User user = getCurrentUser();

        Queue queue = findQueue(id);

        verifyQueueAccess(queue, user);

        queueRepository.delete(queue);
    }

    // =========================
    // BUSINESS QUEUE
    // =========================

    @Override
    public List<QueueResponse> getBusinessQueue(
            Long businessId) {

        verifyBusinessOwnership(businessId);

        Business business = findBusiness(businessId);

        return queueRepository
                .findByAppointment_BusinessOrderByQueueNumberAsc(
                        business
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================
    // CURRENT SERVING
    // =========================

    @Override
    public QueueResponse getCurrentServing(
            Long businessId) {

        verifyBusinessOwnership(businessId);

        Business business = findBusiness(businessId);

        Queue queue = queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.SERVING
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No customer is currently being served"
                        ));

        return mapToResponse(queue);
    }

    // =========================
    // CALL NEXT CUSTOMER
    // =========================

    @Override
    public QueueResponse callNextCustomer(
            Long businessId) {

        verifyBusinessOwnership(businessId);

        Business business = findBusiness(businessId);

        // Complete currently serving customer.
        queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.SERVING
                )
                .ifPresent(queue -> {

                    queue.setStatus(QueueStatus.COMPLETED);

                    queueRepository.save(queue);
                });

        Queue nextQueue = queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.WAITING
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No waiting customers"
                        ));

        nextQueue.setStatus(QueueStatus.SERVING);

        queueRepository.save(nextQueue);

        notificationService.createNotification(
                nextQueue.getAppointment().getUser().getId(),
                "It is now your turn. Please proceed to the counter."
        );

        return mapToResponse(nextQueue);
    }

    // =========================
    // COMPLETE CUSTOMER
    // =========================

    @Override
    public QueueResponse completeCurrentCustomer(
            Long businessId) {

        verifyBusinessOwnership(businessId);

        Business business = findBusiness(businessId);

        Queue currentQueue = queueRepository
                .findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                        business,
                        QueueStatus.SERVING
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No customer is currently being served"
                        ));

        currentQueue.setStatus(QueueStatus.COMPLETED);

        queueRepository.save(currentQueue);

        notificationService.createNotification(
                currentQueue.getAppointment().getUser().getId(),
                "Your appointment has been completed. Thank you for visiting."
        );

        return mapToResponse(currentQueue);
    }

    // =========================
    // CANCEL QUEUE
    // =========================

    @Override
    public QueueResponse cancelQueue(Long id) {

        User user = getCurrentUser();

        Queue queue = findQueue(id);

        /*
         * Customer can cancel their own queue.
         * Business owner can cancel a queue in their business.
         * Admin can cancel any queue.
         */
        if (user.getRole() == Role.USER) {

            if (!queue.getAppointment()
                    .getUser()
                    .getId()
                    .equals(user.getId())) {

                throw new RuntimeException(
                        "You are not authorized to cancel this queue"
                );
            }
        }

        if (user.getRole() == Role.BUSINESS_OWNER) {

            verifyBusinessOwnership(
                    queue.getAppointment()
                            .getBusiness()
                            .getId()
            );
        }

        if (queue.getStatus() == QueueStatus.COMPLETED) {

            throw new RuntimeException(
                    "Completed queue cannot be cancelled"
            );
        }

        if (queue.getStatus() == QueueStatus.CANCELLED) {

            throw new RuntimeException(
                    "Queue is already cancelled"
            );
        }

        queue.setStatus(QueueStatus.CANCELLED);

        queue = queueRepository.save(queue);

        notificationService.createNotification(
                queue.getAppointment().getUser().getId(),
                "Your queue has been cancelled."
        );

        return mapToResponse(queue);
    }

    // =========================
    // QUEUE POSITION
    // =========================

    @Override
    public QueuePositionResponse getQueuePosition(
            Long queueId) {

        User user = getCurrentUser();

        Queue queue = findQueue(queueId);

        verifyQueueAccess(queue, user);

        Business business =
                queue.getAppointment().getBusiness();

        List<Queue> waitingQueues =
                queueRepository
                        .findByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
                                business,
                                QueueStatus.WAITING
                        );

        int peopleAhead = (int) waitingQueues
                .stream()
                .filter(q ->
                        q.getQueueNumber()
                                < queue.getQueueNumber())
                .count();

        int estimatedWait = peopleAhead * 10;

        return QueuePositionResponse.builder()
                .queueId(queue.getId())
                .queueNumber(queue.getQueueNumber())
                .peopleAhead(peopleAhead)
                .estimatedWaitMinutes(estimatedWait)
                .build();
    }

    // =========================
    // ESTIMATED WAIT TIME
    // =========================

    @Override
    public WaitTimeResponse getEstimatedWaitTime(
            Long queueId) {

        // getQueuePosition already performs ownership checking.
        QueuePositionResponse position =
                getQueuePosition(queueId);

        return WaitTimeResponse.builder()
                .queueId(position.getQueueId())
                .estimatedWaitMinutes(
                        position.getEstimatedWaitMinutes()
                )
                .build();
    }

    // =========================
    // QUEUE STATISTICS
    // =========================

    @Override
    public QueueStatisticsResponse getQueueStatistics() {

        User user = getCurrentUser();

        if (user.getRole() != Role.ADMIN) {

            throw new RuntimeException(
                    "Only administrators can view queue statistics"
            );
        }

        return QueueStatisticsResponse.builder()
                .totalQueues(queueRepository.count())
                .waitingCustomers(
                        queueRepository.countByStatus(
                                QueueStatus.WAITING
                        )
                )
                .servingCustomers(
                        queueRepository.countByStatus(
                                QueueStatus.SERVING
                        )
                )
                .completedCustomers(
                        queueRepository.countByStatus(
                                QueueStatus.COMPLETED
                        )
                )
                .cancelledCustomers(
                        queueRepository.countByStatus(
                                QueueStatus.CANCELLED
                        )
                )
                .build();
    }

    // =========================
    // CUSTOMER DASHBOARD
    // =========================

    @Override
    public CustomerDashboardResponse getCustomerDashboard(
            Long appointmentId) {

        User user = getCurrentUser();

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                ));

        if (user.getRole() != Role.ADMIN &&
                !appointment.getUser()
                        .getId()
                        .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to view this dashboard"
            );
        }

        Queue queue =
                queueRepository.findByAppointment(appointment)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Queue not found"
                                ));

        QueuePositionResponse position =
                getQueuePosition(queue.getId());

        return CustomerDashboardResponse.builder()
                .appointmentId(appointment.getId())
                .queueId(queue.getId())
                .businessName(
                        appointment.getBusiness().getName()
                )
                .queueNumber(queue.getQueueNumber())
                .peopleAhead(position.getPeopleAhead())
                .estimatedWaitMinutes(
                        position.getEstimatedWaitMinutes()
                )
                .queueStatus(queue.getStatus())
                .build();
    }

    // =========================
    // BUSINESS OWNERSHIP
    // =========================

    private void verifyBusinessOwnership(
            Long businessId) {

        User user = getCurrentUser();

        // Admin can access any business.
        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() != Role.BUSINESS_OWNER) {

            throw new RuntimeException(
                    "Only business owners can perform this action"
            );
        }

        BusinessOwner owner =
                businessOwnerRepository
                        .findByUser_Email(user.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "You are not a business owner"
                                ));

        if (!owner.getBusiness()
                .getId()
                .equals(businessId)) {

            throw new RuntimeException(
                    "You are not authorized to access this business"
            );
        }
    }

    // =========================
    // QUEUE ACCESS
    // =========================

    private void verifyQueueAccess(
            Queue queue,
            User user) {

        // Admin can access any queue.
        if (user.getRole() == Role.ADMIN) {
            return;
        }

        // Customer can access their own queue.
        if (user.getRole() == Role.USER) {

            if (!queue.getAppointment()
                    .getUser()
                    .getId()
                    .equals(user.getId())) {

                throw new RuntimeException(
                        "You are not authorized to access this queue"
                );
            }

            return;
        }

        // Business owner can access queues
        // belonging to their business.
        if (user.getRole() == Role.BUSINESS_OWNER) {

            verifyBusinessOwnership(
                    queue.getAppointment()
                            .getBusiness()
                            .getId()
            );

            return;
        }

        throw new RuntimeException(
                "You are not authorized to access this queue"
        );
    }

    // =========================
    // CURRENT USER
    // =========================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }

    // =========================
    // FIND QUEUE
    // =========================

    private Queue findQueue(Long id) {

        return queueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue not found"
                        ));
    }

    // =========================
    // FIND BUSINESS
    // =========================

    private Business findBusiness(Long id) {

        return businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));
    }

    // =========================
    // RESPONSE MAPPER
    // =========================

    private QueueResponse mapToResponse(
            Queue queue) {

        return QueueResponse.builder()
                .id(queue.getId())
                .appointmentId(
                        queue.getAppointment().getId()
                )
                .queueNumber(
                        queue.getQueueNumber()
                )
                .status(
                        queue.getStatus()
                )
                .build();
    }
}
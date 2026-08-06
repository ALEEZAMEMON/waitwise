package com.waitwise.backend.service;

import com.waitwise.backend.dto.DashboardResponse;
import com.waitwise.backend.enums.QueueStatus;
import com.waitwise.backend.repository.AppointmentRepository;
import com.waitwise.backend.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AppointmentRepository appointmentRepository;

    private final QueueRepository queueRepository;

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .totalAppointments(appointmentRepository.count())
                .totalQueues(queueRepository.count())
                .waitingCustomers(queueRepository.countByStatus(QueueStatus.WAITING))
                .currentlyServing(queueRepository.countByStatus(QueueStatus.SERVING))
                .completedCustomers(queueRepository.countByStatus(QueueStatus.COMPLETED))
                .cancelledCustomers(queueRepository.countByStatus(QueueStatus.CANCELLED))
                .build();
    }
}
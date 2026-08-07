package com.waitwise.backend.service;

import com.waitwise.backend.dto.BusinessDashboardResponse;
import com.waitwise.backend.dto.DashboardResponse;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.enums.QueueStatus;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.AppointmentRepository;
import com.waitwise.backend.repository.BusinessRepository;
import com.waitwise.backend.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final QueueRepository queueRepository;
    private final BusinessRepository businessRepository;

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

    @Override
    public BusinessDashboardResponse getBusinessDashboard(Long businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        return BusinessDashboardResponse.builder()
                .businessId(business.getId())
                .businessName(business.getName())
                .totalAppointments(
                        appointmentRepository.countByBusiness(business)
                )
                .totalQueues(
                        queueRepository.countByAppointment_Business(business)
                )
                .waitingCustomers(
                        queueRepository.countByAppointment_BusinessAndStatus(
                                business,
                                QueueStatus.WAITING
                        )
                )
                .currentlyServing(
                        queueRepository.countByAppointment_BusinessAndStatus(
                                business,
                                QueueStatus.SERVING
                        )
                )
                .completedCustomers(
                        queueRepository.countByAppointment_BusinessAndStatus(
                                business,
                                QueueStatus.COMPLETED
                        )
                )
                .cancelledCustomers(
                        queueRepository.countByAppointment_BusinessAndStatus(
                                business,
                                QueueStatus.CANCELLED
                        )
                )
                .build();
    }
}
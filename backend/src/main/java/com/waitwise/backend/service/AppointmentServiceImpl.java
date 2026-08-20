package com.waitwise.backend.service;

import com.waitwise.backend.dto.appointment.AppointmentRequest;
import com.waitwise.backend.dto.appointment.AppointmentResponse;
import com.waitwise.backend.dto.appointment.UpdateAppointmentStatusRequest;
import com.waitwise.backend.entity.Appointment;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.enums.AppointmentStatus;
import com.waitwise.backend.enums.Role;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.AppointmentRepository;
import com.waitwise.backend.repository.BusinessRepository;
import com.waitwise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;


    // =========================
    // CREATE APPOINTMENT
    // =========================

    @Override
    public AppointmentResponse createAppointment(
            AppointmentRequest request) {

        User user = getCurrentUser();

        // Only customers and admins can create appointments
        if (user.getRole() != Role.USER &&
                user.getRole() != Role.ADMIN) {

            throw new RuntimeException(
                    "Only customers can create appointments"
            );
        }

        Business business = businessRepository.findById(
                request.getBusinessId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Business not found"
                ));

        Appointment appointment = Appointment.builder()
                .business(business)
                .user(user)
                .appointmentTime(request.getAppointmentTime())
                .status(AppointmentStatus.PENDING)
                .build();

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }


    // =========================
    // GET APPOINTMENTS
    // =========================

    @Override
    public List<AppointmentResponse> getAllAppointments() {

        User user = getCurrentUser();

        /*
         * ADMIN can see all appointments.
         *
         * CUSTOMER only sees their own appointments.
         */
        if (user.getRole() == Role.ADMIN) {

            return appointmentRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        return appointmentRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================
    // GET APPOINTMENT BY ID
    // =========================

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        User user = getCurrentUser();

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        verifyAppointmentAccess(appointment, user);

        return mapToResponse(appointment);
    }


    // =========================
    // UPDATE APPOINTMENT
    // =========================

    @Override
    public AppointmentResponse updateAppointment(
            Long id,
            AppointmentRequest request) {

        User user = getCurrentUser();

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        verifyAppointmentAccess(appointment, user);

        Business business = businessRepository.findById(
                request.getBusinessId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Business not found"
                ));

        appointment.setBusiness(business);
        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }


    // =========================
    // UPDATE APPOINTMENT STATUS
    // =========================

    @Override
    public AppointmentResponse updateAppointmentStatus(
            Long id,
            UpdateAppointmentStatusRequest request) {

        User user = getCurrentUser();

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        /*
         * Only ADMIN can directly change appointment status.
         *
         * Normal customer status changes should happen
         * through the proper appointment/queue workflow.
         */
        if (user.getRole() != Role.ADMIN) {

            throw new RuntimeException(
                    "Only administrators can change appointment status"
            );
        }

        appointment.setStatus(request.getStatus());

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }


    // =========================
    // DELETE APPOINTMENT
    // =========================

    @Override
    public void deleteAppointment(Long id) {

        User user = getCurrentUser();

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        verifyAppointmentAccess(appointment, user);

        appointmentRepository.delete(appointment);
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
    // APPOINTMENT OWNERSHIP
    // =========================

    private void verifyAppointmentAccess(
            Appointment appointment,
            User user) {

        /*
         * ADMIN has access to every appointment.
         */
        if (user.getRole() == Role.ADMIN) {
            return;
        }

        /*
         * Normal users can only access
         * appointments that belong to them.
         */
        if (!appointment.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to access this appointment"
            );
        }
    }


    // =========================
    // RESPONSE MAPPER
    // =========================

    private AppointmentResponse mapToResponse(
            Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .businessId(
                        appointment.getBusiness().getId()
                )
                .businessName(
                        appointment.getBusiness().getName()
                )
                .appointmentTime(
                        appointment.getAppointmentTime()
                )
                .status(
                        appointment.getStatus()
                )
                .build();
    }
}
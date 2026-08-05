package com.waitwise.backend.service;

import com.waitwise.backend.dto.AppointmentRequest;
import com.waitwise.backend.dto.AppointmentResponse;
import com.waitwise.backend.entity.Appointment;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.User;
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

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Appointment appointment = Appointment.builder()
                .business(business)
                .user(user)
                .appointmentTime(request.getAppointmentTime())
                .status(request.getStatus())
                .build();

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return appointmentRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        return mapToResponse(appointment);
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        appointment.setBusiness(business);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(request.getStatus());

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointmentRepository.delete(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .businessId(appointment.getBusiness().getId())
                .businessName(appointment.getBusiness().getName())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .build();
    }
}
package com.waitwise.backend.service;

import com.waitwise.backend.dto.appointment.AppointmentRequest;
import com.waitwise.backend.dto.appointment.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAllAppointments();

    AppointmentResponse getAppointmentById(Long id);

    AppointmentResponse updateAppointment(Long id, AppointmentRequest request);

    void deleteAppointment(Long id);
}
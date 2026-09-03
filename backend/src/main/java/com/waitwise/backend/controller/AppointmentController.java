package com.waitwise.backend.controller;

import com.waitwise.backend.dto.appointment.AppointmentRequest;
import com.waitwise.backend.dto.appointment.AppointmentResponse;
import com.waitwise.backend.dto.appointment.UpdateAppointmentStatusRequest;
import com.waitwise.backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // =========================
    // CREATE APPOINTMENT
    // =========================

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AppointmentResponse createAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        return appointmentService.createAppointment(request);
    }

    // =========================
    // GET APPOINTMENTS
    // =========================

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentService.getAllAppointments();
    }


    // =========================
    // GET APPOINTMENT BY ID
    // =========================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AppointmentResponse getAppointmentById(
            @PathVariable Long id) {

        return appointmentService.getAppointmentById(id);
    }


    // =========================
    // UPDATE APPOINTMENT
    // =========================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AppointmentResponse updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest request) {

        return appointmentService.updateAppointment(id, request);
    }

    // =========================
    // UPDATE APPOINTMENT STATUS
    // =========================

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public AppointmentResponse updateAppointmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {

        return appointmentService.updateAppointmentStatus(
                id,
                request
        );
    }

    // =========================
    // DELETE APPOINTMENT
    // =========================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return "Appointment deleted successfully";
    }
}
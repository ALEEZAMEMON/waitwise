package com.waitwise.backend.repository;

import com.waitwise.backend.entity.Appointment;
import com.waitwise.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser(User user);

}
package com.waitwise.backend.repository;

import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.Queue;
import com.waitwise.backend.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    Queue findTopByOrderByQueueNumberDesc();

    List<Queue> findByAppointment_BusinessOrderByQueueNumberAsc(Business business);

    Optional<Queue> findFirstByAppointment_BusinessAndStatusOrderByQueueNumberAsc(
            Business business,
            QueueStatus status
    );
}
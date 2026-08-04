package com.waitwise.backend.repository;

import com.waitwise.backend.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    Queue findTopByOrderByQueueNumberDesc();

}
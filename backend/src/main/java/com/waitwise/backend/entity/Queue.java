package com.waitwise.backend.entity;

import com.waitwise.backend.enums.QueueStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "queues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false)
    private Integer queueNumber;

    @Enumerated(EnumType.STRING)
    private QueueStatus status;
}
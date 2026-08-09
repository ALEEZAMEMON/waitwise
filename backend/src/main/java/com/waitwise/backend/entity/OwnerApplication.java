package com.waitwise.backend.entity;

import com.waitwise.backend.enums.OwnerApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "owner_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String cnic;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessType;

    @Column(nullable = false)
    private String businessAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerApplicationStatus status;
}
package com.waitwise.backend.repository;

import com.waitwise.backend.entity.OwnerApplication;
import com.waitwise.backend.enums.OwnerApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerApplicationRepository
        extends JpaRepository<OwnerApplication, Long> {

    Optional<OwnerApplication> findByUserId(Long userId);

    List<OwnerApplication> findByStatus(OwnerApplicationStatus status);

    boolean existsByUserId(Long userId);
}
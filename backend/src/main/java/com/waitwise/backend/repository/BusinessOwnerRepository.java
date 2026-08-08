package com.waitwise.backend.repository;

import com.waitwise.backend.entity.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessOwnerRepository
        extends JpaRepository<BusinessOwner, Long> {

    Optional<BusinessOwner> findByUser_Email(String email);
}
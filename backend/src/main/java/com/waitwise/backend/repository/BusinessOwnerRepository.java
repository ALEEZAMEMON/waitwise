package com.waitwise.backend.repository;

import com.waitwise.backend.entity.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Long> {
}
package com.waitwise.backend.repository;

import com.waitwise.backend.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByName(String name);

    boolean existsByName(String name);

}
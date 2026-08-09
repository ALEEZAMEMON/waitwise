package com.waitwise.backend.controller;

import com.waitwise.backend.dto.ownerapplication.OwnerApplicationRequest;
import com.waitwise.backend.dto.ownerapplication.OwnerApplicationResponse;
import com.waitwise.backend.service.OwnerApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner-applications")
@RequiredArgsConstructor
public class OwnerApplicationController {

    private final OwnerApplicationService ownerApplicationService;

    @PostMapping
    public OwnerApplicationResponse createApplication(
            @Valid @RequestBody OwnerApplicationRequest request) {

        return ownerApplicationService.createApplication(request);
    }

    @GetMapping
    public List<OwnerApplicationResponse> getAllApplications() {

        return ownerApplicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public OwnerApplicationResponse getApplicationById(
            @PathVariable Long id) {

        return ownerApplicationService.getApplicationById(id);
    }

    @GetMapping("/pending")
    public List<OwnerApplicationResponse> getPendingApplications() {

        return ownerApplicationService.getPendingApplications();
    }

    @PutMapping("/{id}/approve")
    public OwnerApplicationResponse approveApplication(
            @PathVariable Long id) {

        return ownerApplicationService.approveApplication(id);
    }

    @PutMapping("/{id}/reject")
    public OwnerApplicationResponse rejectApplication(
            @PathVariable Long id) {

        return ownerApplicationService.rejectApplication(id);
    }
}
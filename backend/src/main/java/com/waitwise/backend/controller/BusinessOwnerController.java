package com.waitwise.backend.controller;

import com.waitwise.backend.dto.business.BusinessOwnerRequest;
import com.waitwise.backend.dto.business.BusinessOwnerResponse;
import com.waitwise.backend.service.BusinessOwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-owners")
@RequiredArgsConstructor
public class BusinessOwnerController {

    private final BusinessOwnerService businessOwnerService;

    @PostMapping
    public BusinessOwnerResponse createBusinessOwner(
            @Valid @RequestBody BusinessOwnerRequest request) {

        return businessOwnerService.createBusinessOwner(request);
    }

    @GetMapping
    public List<BusinessOwnerResponse> getAllBusinessOwners() {

        return businessOwnerService.getAllBusinessOwners();
    }

    @GetMapping("/{id}")
    public BusinessOwnerResponse getBusinessOwnerById(@PathVariable Long id) {

        return businessOwnerService.getBusinessOwnerById(id);
    }

    @PutMapping("/{id}")
    public BusinessOwnerResponse updateBusinessOwner(
            @PathVariable Long id,
            @Valid @RequestBody BusinessOwnerRequest request) {

        return businessOwnerService.updateBusinessOwner(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteBusinessOwner(@PathVariable Long id) {

        businessOwnerService.deleteBusinessOwner(id);

        return "Business Owner deleted successfully";
    }
}
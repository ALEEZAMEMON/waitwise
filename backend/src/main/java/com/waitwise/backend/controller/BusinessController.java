package com.waitwise.backend.controller;

import com.waitwise.backend.dto.business.BusinessRequest;
import com.waitwise.backend.dto.business.BusinessResponse;
import com.waitwise.backend.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;


    @PostMapping
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public BusinessResponse createBusiness(
            @Valid @RequestBody BusinessRequest request) {

        return businessService.createBusiness(request);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'BUSINESS_OWNER', 'ADMIN')")
    public List<BusinessResponse> getAllBusinesses() {

        return businessService.getAllBusinesses();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'BUSINESS_OWNER', 'ADMIN')")
    public BusinessResponse getBusinessById(
            @PathVariable Long id) {

        return businessService.getBusinessById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public BusinessResponse updateBusiness(
            @PathVariable Long id,
            @Valid @RequestBody BusinessRequest request) {

        return businessService.updateBusiness(id, request);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN')")
    public String deleteBusiness(
            @PathVariable Long id) {

        businessService.deleteBusiness(id);

        return "Business deleted successfully";
    }
}
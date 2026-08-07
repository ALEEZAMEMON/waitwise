package com.waitwise.backend.service;

import com.waitwise.backend.dto.business.BusinessOwnerRequest;
import com.waitwise.backend.dto.business.BusinessOwnerResponse;

import java.util.List;

public interface BusinessOwnerService {

    BusinessOwnerResponse createBusinessOwner(BusinessOwnerRequest request);

    List<BusinessOwnerResponse> getAllBusinessOwners();

    BusinessOwnerResponse getBusinessOwnerById(Long id);

    BusinessOwnerResponse updateBusinessOwner(Long id, BusinessOwnerRequest request);

    void deleteBusinessOwner(Long id);
}
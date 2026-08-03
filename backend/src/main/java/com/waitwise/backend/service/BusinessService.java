package com.waitwise.backend.service;

import com.waitwise.backend.dto.BusinessRequest;
import com.waitwise.backend.dto.BusinessResponse;

import java.util.List;

public interface BusinessService {

    BusinessResponse createBusiness(BusinessRequest request);

    List<BusinessResponse> getAllBusinesses();

    BusinessResponse getBusinessById(Long id);

    BusinessResponse updateBusiness(Long id, BusinessRequest request);

    void deleteBusiness(Long id);
}

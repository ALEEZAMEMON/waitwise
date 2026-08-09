package com.waitwise.backend.service;

import com.waitwise.backend.dto.ownerapplication.OwnerApplicationRequest;
import com.waitwise.backend.dto.ownerapplication.OwnerApplicationResponse;

import java.util.List;

public interface OwnerApplicationService {

    OwnerApplicationResponse createApplication(
            OwnerApplicationRequest request
    );

    List<OwnerApplicationResponse> getAllApplications();

    OwnerApplicationResponse getApplicationById(Long id);

    List<OwnerApplicationResponse> getPendingApplications();

    OwnerApplicationResponse approveApplication(Long id);

    OwnerApplicationResponse rejectApplication(Long id);
}
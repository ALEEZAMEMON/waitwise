package com.waitwise.backend.service;

import com.waitwise.backend.dto.ownerapplication.OwnerApplicationRequest;
import com.waitwise.backend.dto.ownerapplication.OwnerApplicationResponse;
import com.waitwise.backend.entity.OwnerApplication;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.enums.OwnerApplicationStatus;
import com.waitwise.backend.enums.Role;
import com.waitwise.backend.repository.OwnerApplicationRepository;
import com.waitwise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerApplicationServiceImpl implements OwnerApplicationService {

    private final OwnerApplicationRepository ownerApplicationRepository;
    private final UserRepository userRepository;

    @Override
    public OwnerApplicationResponse createApplication(
            OwnerApplicationRequest request) {

        if (ownerApplicationRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException(
                    "You have already submitted a business owner application"
            );
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (user.getRole() == Role.BUSINESS_OWNER) {
            throw new RuntimeException(
                    "User is already a business owner"
            );
        }

        OwnerApplication application = OwnerApplication.builder()
                .user(user)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .cnic(request.getCnic())
                .businessName(request.getBusinessName())
                .businessType(request.getBusinessType())
                .businessAddress(request.getBusinessAddress())
                .status(OwnerApplicationStatus.PENDING)
                .build();

        application = ownerApplicationRepository.save(application);

        return mapToResponse(application);
    }

    @Override
    public List<OwnerApplicationResponse> getAllApplications() {

        return ownerApplicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OwnerApplicationResponse getApplicationById(Long id) {

        OwnerApplication application =
                ownerApplicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner application not found"
                                ));

        return mapToResponse(application);
    }

    @Override
    public List<OwnerApplicationResponse> getPendingApplications() {

        return ownerApplicationRepository
                .findByStatus(OwnerApplicationStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OwnerApplicationResponse approveApplication(Long id) {

        OwnerApplication application =
                ownerApplicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner application not found"
                                ));

        if (application.getStatus() != OwnerApplicationStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending applications can be approved"
            );
        }

        User user = application.getUser();

        user.setRole(Role.BUSINESS_OWNER);
        userRepository.save(user);

        application.setStatus(OwnerApplicationStatus.APPROVED);

        application = ownerApplicationRepository.save(application);

        return mapToResponse(application);
    }

    @Override
    public OwnerApplicationResponse rejectApplication(Long id) {

        OwnerApplication application =
                ownerApplicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner application not found"
                                ));

        if (application.getStatus() != OwnerApplicationStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending applications can be rejected"
            );
        }

        application.setStatus(OwnerApplicationStatus.REJECTED);

        application = ownerApplicationRepository.save(application);

        return mapToResponse(application);
    }

    private OwnerApplicationResponse mapToResponse(
            OwnerApplication application) {

        return OwnerApplicationResponse.builder()
                .id(application.getId())
                .userId(application.getUser().getId())
                .fullName(application.getFullName())
                .email(application.getEmail())
                .phoneNumber(application.getPhoneNumber())
                .cnic(application.getCnic())
                .businessName(application.getBusinessName())
                .businessType(application.getBusinessType())
                .businessAddress(application.getBusinessAddress())
                .status(application.getStatus())
                .build();
    }
}
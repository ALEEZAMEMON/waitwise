package com.waitwise.backend.service;
import com.waitwise.backend.dto.business.BusinessOwnerRequest;
import com.waitwise.backend.dto.business.BusinessOwnerResponse;
import com.waitwise.backend.entity.BusinessOwner;
import com.waitwise.backend.repository.BusinessOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessOwnerServiceImpl implements BusinessOwnerService {

    private final BusinessOwnerRepository businessOwnerRepository;

    @Override
    public BusinessOwnerResponse createBusinessOwner(BusinessOwnerRequest request) {

        BusinessOwner owner = BusinessOwner.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();

        owner = businessOwnerRepository.save(owner);

        return mapToResponse(owner);
    }

    @Override
    public List<BusinessOwnerResponse> getAllBusinessOwners() {

        return businessOwnerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BusinessOwnerResponse getBusinessOwnerById(Long id) {

        BusinessOwner owner = businessOwnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Owner not found"));

        return mapToResponse(owner);
    }

    @Override
    public BusinessOwnerResponse updateBusinessOwner(Long id, BusinessOwnerRequest request) {

        BusinessOwner owner = businessOwnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Owner not found"));

        owner.setFullName(request.getFullName());
        owner.setEmail(request.getEmail());
        owner.setPhoneNumber(request.getPhoneNumber());

        owner = businessOwnerRepository.save(owner);

        return mapToResponse(owner);
    }

    @Override
    public void deleteBusinessOwner(Long id) {

        BusinessOwner owner = businessOwnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business Owner not found"));

        businessOwnerRepository.delete(owner);
    }

    private BusinessOwnerResponse mapToResponse(BusinessOwner owner) {

        return BusinessOwnerResponse.builder()
                .id(owner.getId())
                .fullName(owner.getFullName())
                .email(owner.getEmail())
                .phoneNumber(owner.getPhoneNumber())
                .build();
    }
}
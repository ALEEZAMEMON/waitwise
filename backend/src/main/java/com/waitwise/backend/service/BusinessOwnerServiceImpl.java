package com.waitwise.backend.service;

import com.waitwise.backend.dto.business.BusinessOwnerRequest;
import com.waitwise.backend.dto.business.BusinessOwnerResponse;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.BusinessOwner;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.BusinessOwnerRepository;
import com.waitwise.backend.repository.BusinessRepository;
import com.waitwise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessOwnerServiceImpl implements BusinessOwnerService {

    private final BusinessOwnerRepository businessOwnerRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    @Override
    public BusinessOwnerResponse createBusinessOwner(BusinessOwnerRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        BusinessOwner owner = BusinessOwner.builder()
                .user(user)
                .business(business)
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business Owner not found"));

        return mapToResponse(owner);
    }

    @Override
    public BusinessOwnerResponse updateBusinessOwner(
            Long id,
            BusinessOwnerRequest request) {

        BusinessOwner owner = businessOwnerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business Owner not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business not found"));

        owner.setUser(user);
        owner.setBusiness(business);
        owner.setFullName(request.getFullName());
        owner.setEmail(request.getEmail());
        owner.setPhoneNumber(request.getPhoneNumber());

        owner = businessOwnerRepository.save(owner);

        return mapToResponse(owner);
    }

    @Override
    public void deleteBusinessOwner(Long id) {

        BusinessOwner owner = businessOwnerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Business Owner not found"));

        businessOwnerRepository.delete(owner);
    }
    

        private BusinessOwnerResponse mapToResponse(BusinessOwner owner) {

            return BusinessOwnerResponse.builder()
                    .id(owner.getId())
                    .userId(owner.getUser().getId())
                    .businessId(owner.getBusiness().getId())
                    .businessName(owner.getBusiness().getName())
                    .fullName(owner.getFullName())
                    .email(owner.getEmail())
                    .phoneNumber(owner.getPhoneNumber())
                    .build();
        }
}
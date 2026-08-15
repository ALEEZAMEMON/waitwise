package com.waitwise.backend.service;

import com.waitwise.backend.dto.business.BusinessRequest;
import com.waitwise.backend.dto.business.BusinessResponse;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.entity.BusinessOwner;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.enums.Role;
import com.waitwise.backend.exception.ResourceNotFoundException;
import com.waitwise.backend.repository.BusinessOwnerRepository;
import com.waitwise.backend.repository.BusinessRepository;
import com.waitwise.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final UserRepository userRepository;


    @Override
    public BusinessResponse createBusiness(BusinessRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.BUSINESS_OWNER) {
            throw new RuntimeException(
                    "Only approved business owners can create a business"
            );
        }

        if (businessOwnerRepository.findByUser_Email(email).isPresent()) {
            throw new RuntimeException(
                    "You already have a business"
            );
        }

        if (businessRepository.existsByName(request.getName())) {
            throw new RuntimeException(
                    "Business already exists"
            );
        }

        Business business = Business.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .build();

        business = businessRepository.save(business);


        BusinessOwner businessOwner = BusinessOwner.builder()
                .user(user)
                .business(business)
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();

        businessOwnerRepository.save(businessOwner);

        return mapToResponse(business);
    }


    @Override
    public List<BusinessResponse> getAllBusinesses() {

        return businessRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public BusinessResponse getBusinessById(Long id) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));

        return mapToResponse(business);
    }


    @Override
    public BusinessResponse updateBusiness(
            Long id,
            BusinessRequest request) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (user.getRole() != Role.ADMIN) {

            BusinessOwner owner =
                    businessOwnerRepository
                            .findByUser_Email(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "You do not own a business"
                                    ));

            if (!owner.getBusiness().getId().equals(id)) {
                throw new RuntimeException(
                        "You are not authorized to update this business"
                );
            }
        }

        business.setName(request.getName());
        business.setDescription(request.getDescription());
        business.setAddress(request.getAddress());
        business.setPhoneNumber(request.getPhoneNumber());
        business.setOpeningTime(request.getOpeningTime());
        business.setClosingTime(request.getClosingTime());

        business = businessRepository.save(business);

        return mapToResponse(business);
    }


    @Override
    public void deleteBusiness(Long id) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (user.getRole() != Role.ADMIN) {

            BusinessOwner owner =
                    businessOwnerRepository
                            .findByUser_Email(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "You do not own a business"
                                    ));

            if (!owner.getBusiness().getId().equals(id)) {
                throw new RuntimeException(
                        "You are not authorized to delete this business"
                );
            }
        }

        businessRepository.delete(business);
    }


    private BusinessResponse mapToResponse(Business business) {

        return BusinessResponse.builder()
                .id(business.getId())
                .name(business.getName())
                .description(business.getDescription())
                .address(business.getAddress())
                .phoneNumber(business.getPhoneNumber())
                .openingTime(business.getOpeningTime())
                .closingTime(business.getClosingTime())
                .build();
    }
}
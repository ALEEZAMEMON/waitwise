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


    // =========================
    // CREATE BUSINESS
    // =========================

    @Override
    public BusinessResponse createBusiness(
            BusinessRequest request) {

        User user = getCurrentUser();

        /*
         * Only approved business owners can create
         * their business.
         */
        if (user.getRole() != Role.BUSINESS_OWNER) {

            throw new RuntimeException(
                    "Only approved business owners can create a business"
            );
        }

        /*
         * One business per business owner.
         */
        if (businessOwnerRepository
                .findByUser_Email(user.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "You already have a business"
            );
        }

        /*
         * Business name must be unique.
         */
        if (businessRepository.existsByName(
                request.getName())) {

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

        /*
         * Create the relationship between
         * the approved owner and the business.
         */
        BusinessOwner businessOwner =
                BusinessOwner.builder()
                        .user(user)
                        .business(business)
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phoneNumber(
                                request.getPhoneNumber()
                        )
                        .build();

        businessOwnerRepository.save(businessOwner);

        return mapToResponse(business);
    }


    // =========================
    // GET ALL BUSINESSES
    // =========================

    @Override
    public List<BusinessResponse> getAllBusinesses() {

        return businessRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================
    // GET BUSINESS
    // =========================

    @Override
    public BusinessResponse getBusinessById(
            Long id) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));

        return mapToResponse(business);
    }


    // =========================
    // UPDATE BUSINESS
    // =========================

    @Override
    public BusinessResponse updateBusiness(
            Long id,
            BusinessRequest request) {

        User user = getCurrentUser();

        Business business = businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));

        /*
         * Admin can update any business.
         */
        if (user.getRole() != Role.ADMIN) {

            verifyBusinessOwnership(id);
        }

        /*
         * Prevent changing the business name
         * to another existing business name.
         */
        if (!business.getName()
                .equals(request.getName())
                && businessRepository.existsByName(
                request.getName())) {

            throw new RuntimeException(
                    "Business with this name already exists"
            );
        }

        business.setName(request.getName());
        business.setDescription(
                request.getDescription()
        );
        business.setAddress(
                request.getAddress()
        );
        business.setPhoneNumber(
                request.getPhoneNumber()
        );
        business.setOpeningTime(
                request.getOpeningTime()
        );
        business.setClosingTime(
                request.getClosingTime()
        );

        business = businessRepository.save(business);

        return mapToResponse(business);
    }


    // =========================
    // DELETE BUSINESS
    // =========================

    @Override
    public void deleteBusiness(Long id) {

        User user = getCurrentUser();

        Business business = businessRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Business not found"
                        ));

        /*
         * Admin can delete any business.
         */
        if (user.getRole() != Role.ADMIN) {

            verifyBusinessOwnership(id);
        }

        businessRepository.delete(business);
    }


    // =========================
    // BUSINESS OWNERSHIP
    // =========================

    private void verifyBusinessOwnership(
            Long businessId) {

        User user = getCurrentUser();

        if (user.getRole() != Role.BUSINESS_OWNER) {

            throw new RuntimeException(
                    "You are not authorized to manage this business"
            );
        }

        BusinessOwner owner =
                businessOwnerRepository
                        .findByUser_Email(user.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "You do not own a business"
                                ));

        if (!owner.getBusiness()
                .getId()
                .equals(businessId)) {

            throw new RuntimeException(
                    "You are not authorized to manage this business"
            );
        }
    }

    // =========================
    // CURRENT USER
    // =========================


    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));
    }


    // =========================
    // RESPONSE MAPPER
    // =========================

    private BusinessResponse mapToResponse(
            Business business) {

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
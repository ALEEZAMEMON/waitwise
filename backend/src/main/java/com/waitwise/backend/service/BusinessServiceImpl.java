package com.waitwise.backend.service;

import com.waitwise.backend.dto.BusinessRequest;
import com.waitwise.backend.dto.BusinessResponse;
import com.waitwise.backend.entity.Business;
import com.waitwise.backend.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    @Override
    public BusinessResponse createBusiness(BusinessRequest request) {

        if (businessRepository.existsByName(request.getName())) {
            throw new RuntimeException("Business already exists");
        }

        Business business = Business.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .build();

        businessRepository.save(business);

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

    @Override
    public List<BusinessResponse> getAllBusinesses() {

        return businessRepository.findAll()
                .stream()
                .map(business -> BusinessResponse.builder()
                        .id(business.getId())
                        .name(business.getName())
                        .description(business.getDescription())
                        .address(business.getAddress())
                        .phoneNumber(business.getPhoneNumber())
                        .openingTime(business.getOpeningTime())
                        .closingTime(business.getClosingTime())
                        .build())
                .toList();
    }

    @Override
    public BusinessResponse getBusinessById(Long id) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));

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

    @Override
    public BusinessResponse updateBusiness(Long id, BusinessRequest request) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        business.setName(request.getName());
        business.setDescription(request.getDescription());
        business.setAddress(request.getAddress());
        business.setPhoneNumber(request.getPhoneNumber());
        business.setOpeningTime(request.getOpeningTime());
        business.setClosingTime(request.getClosingTime());

        businessRepository.save(business);

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

    @Override
    public void deleteBusiness(Long id) {

        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        businessRepository.delete(business);
    }
}

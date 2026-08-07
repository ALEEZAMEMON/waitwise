package com.waitwise.backend.dto.business;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String openingTime;

    @NotBlank
    private String closingTime;
}
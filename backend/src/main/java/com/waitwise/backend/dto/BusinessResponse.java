package com.waitwise.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusinessResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private String openingTime;
    private String closingTime;
}
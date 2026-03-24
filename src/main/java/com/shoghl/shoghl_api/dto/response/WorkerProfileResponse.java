package com.shoghl.shoghl_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class WorkerProfileResponse {
    private UUID id;
    private UUID userId;
    private String fullName;
    private String email;
    private String phone;
    private String bio;
    private Integer yearsExperience;
    private String city;
    private String area;
    private boolean isAvailable;
    private BigDecimal avgRating;
    private Integer totalReviews;
    private Set<String> categories;
}
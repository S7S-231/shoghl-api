package com.shoghl.shoghl_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class WorkerProfileRequest {

    @NotBlank(message = "Bio is required")
    private String bio;

    @NotNull(message = "Years of experience is required")
    private Integer yearsExperience;

    @NotBlank(message = "City is required")
    private String city;

    private String area;

    @NotNull(message = "Categories are required")
    private Set<Integer> categoryIds;
}
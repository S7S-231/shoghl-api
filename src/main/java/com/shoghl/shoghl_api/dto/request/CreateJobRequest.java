package com.shoghl.shoghl_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateJobRequest {

    @NotNull(message = "Category is required")
    private Integer categoryId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "City is required")
    private String city;

    private String area;
    private BigDecimal budget;
}
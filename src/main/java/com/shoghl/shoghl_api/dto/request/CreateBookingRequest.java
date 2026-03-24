package com.shoghl.shoghl_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateBookingRequest {

    @NotNull(message = "Job ID is required")
    private UUID jobId;

    @NotNull(message = "Price offered is required")
    private BigDecimal priceOffered;

    private String message;
}
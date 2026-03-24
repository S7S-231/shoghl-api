package com.shoghl.shoghl_api.dto.response;

import com.shoghl.shoghl_api.model.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {
    private UUID id;
    private UUID jobId;
    private String jobTitle;
    private UUID workerId;
    private String workerName;
    private BigDecimal priceOffered;
    private String message;
    private BookingStatus status;
    private LocalDateTime createdAt;
}
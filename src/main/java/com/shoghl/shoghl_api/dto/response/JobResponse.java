package com.shoghl.shoghl_api.dto.response;

import com.shoghl.shoghl_api.model.enums.JobStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class JobResponse {
    private UUID id;
    private UUID clientId;
    private String clientName;
    private Integer categoryId;
    private String categoryName;
    private String title;
    private String description;
    private String city;
    private String area;
    private BigDecimal budget;
    private JobStatus status;
    private LocalDateTime createdAt;
}
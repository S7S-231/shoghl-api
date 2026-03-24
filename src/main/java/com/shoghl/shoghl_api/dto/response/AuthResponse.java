package com.shoghl.shoghl_api.dto.response;

import com.shoghl.shoghl_api.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String fullName;
    private String email;
    private UserRole role;
}
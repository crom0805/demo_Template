package com.demo.config.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfo {
    @Schema(description = "위임방법(Bearer 고정값)", nullable = false, example = "Bearer")
    private String grantType;
    @Schema(description = "accessToken", nullable = false, example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiYXV0aCI6IiIsImV4cCI6MTY5MDg3NjA5Nn0.vVf5E8Wn1pcKMHXL5J_gp5F8o4e_hW-RlrvpQCGSgic")
    private String accessToken;
    @Schema(description = "refreshToken", nullable = false, example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTA4NzYwOTZ9.O7zoMVk9-lUghNIFqPBCOT_sX7ppYhQBayZo6jaHjRQ")
    private String refreshToken;
}

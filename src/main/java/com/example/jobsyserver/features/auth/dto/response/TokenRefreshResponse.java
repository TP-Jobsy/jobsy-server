package com.example.jobsyserver.features.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с новыми токенами после обновления")
public class TokenRefreshResponse {

    @Schema(description = "Новый JWT access-токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String accessToken;

    @Schema(description = "Новый refresh-токен", example = "e7d1f4a9-3c5b-4d6e-8b2a-1f2e3d4c5b6a")
    private String refreshToken;

    @Schema(description = "Время истечения действия refresh-токена (UTC)", example = "2025-06-01T12:00:00Z")
    private Instant refreshTokenExpiry;
}

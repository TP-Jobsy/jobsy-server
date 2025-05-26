package com.example.jobsyserver.features.auth.dto.response;

import com.example.jobsyserver.features.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Ответ при успешной аутентификации пользователя")
public class AuthenticationResponse {

    @Schema(description = "JWT access токен для авторизации", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String accessToken;

    @Schema(description = "JWT refresh токен для обновления access токена", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private String refreshToken;

    @Schema(description = "Время истечения действия refresh токена (UTC)", example = "2025-06-01T12:00:00Z")
    private Instant refreshTokenExpiry;

    @Schema(description = "Информация о пользователе")
    private UserDto user;
}

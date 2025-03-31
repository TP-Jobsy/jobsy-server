package com.example.jobsyserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос для аутентификации пользователя (вход в систему)")
public class AuthenticationRequest {

    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Пароль пользователя", example = "SecretPassword123!")
    private String password;
}

package com.example.jobsyserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на восстановление пароля")
public class PasswordResetRequest {
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;
}

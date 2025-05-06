package com.example.jobsyserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос для аутентификации пользователя (вход в систему)")
public class AuthenticationRequest {

    @NotBlank(message = "E-mail не может быть пустым")
    @Email(message = "Неверный формат e-mail")
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль пользователя", example = "SecretPassword123!")
    private String password;
}

package com.example.jobsyserver.dto.response;

import com.example.jobsyserver.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ при успешной аутентификации пользователя")
public class AuthenticationResponse {

    @Schema(description = "JWT токен для авторизации", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    private String token;

    @Schema(description = "Информация о пользователе")
    private User user;
}

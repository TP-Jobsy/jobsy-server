package com.example.jobsyserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на подтверждение восстановления пароля")
public class PasswordResetConfirmRequest {
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;

    @JsonProperty("confirmationCode")
    @Schema(description = "Код восстановления", example = "1234")
    private String resetCode;

    @Schema(description = "Новый пароль", example = "NewSecretPassword123!")
    private String newPassword;
}

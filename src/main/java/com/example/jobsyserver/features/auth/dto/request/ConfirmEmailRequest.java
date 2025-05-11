package com.example.jobsyserver.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Данные для подтверждения почты")
public class ConfirmEmailRequest {

    @NotBlank(message = "E-mail не может быть пустым")
    @Email(message = "Неверный формат e-mail")
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;

    @NotBlank(message = "Код подтверждения не может быть пустым")
    @Schema(description = "Код подтверждения", example = "1234")
    private String confirmationCode;
}

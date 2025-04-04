package com.example.jobsyserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для подтверждения почты")
public class ConfirmEmailRequest {
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Код подтверждения", example = "1234")
    private String confirmationCode;
}

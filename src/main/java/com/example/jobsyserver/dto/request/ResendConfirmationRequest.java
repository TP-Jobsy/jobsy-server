package com.example.jobsyserver.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на повторную отправку кода подтверждения e-mail")
public class ResendConfirmationRequest {
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;
}

package com.example.jobsyserver.features.auth.dto.confirmation;

import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "DTO для подтверждения действия")
public class ConfirmationDto {

    @Schema(description = "Идентификатор подтверждения", example = "1")
    private Long id;

    @Schema(description = "Идентификатор пользователя", example = "123")
    private Long userId;

    @Schema(description = "Тип действия подтверждения", example = "REGISTRATION")
    private ConfirmationAction action;

    @Schema(description = "Код подтверждения", example = "1234")
    private String confirmationCode;

    @Schema(description = "Дата и время истечения срока действия", example = "2025-04-01T12:00:00")
    private LocalDateTime expiresAt;

    @Schema(description = "Флаг использования подтверждения", example = "false")
    private Boolean used;

    @Schema(description = "Дата создания подтверждения", example = "2025-03-31T12:00:00")
    private LocalDateTime createdAt;
}
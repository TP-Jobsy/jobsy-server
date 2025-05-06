package com.example.jobsyserver.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип заявки")
public enum ApplicationType {
    @Schema(description = "Отклик")
    RESPONSE,

    @Schema(description = "Приглашение")
    INVITATION
}

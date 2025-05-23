package com.example.jobsyserver.features.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип действия для подтверждения")
public enum ConfirmationAction {
    @Schema(description = "Подтверждение регистрации")
    REGISTRATION,

    @Schema(description = "Сброс пароля")
    PASSWORD_RESET,
    @Schema(description = "Подтверждение входа в админ-панель")
    ADMIN_LOGIN
}

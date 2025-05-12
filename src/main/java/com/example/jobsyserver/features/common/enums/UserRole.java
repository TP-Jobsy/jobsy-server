package com.example.jobsyserver.features.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Роль пользователя в системе")
public enum UserRole {
    @Schema(description = "Клиент")
    CLIENT,

    @Schema(description = "Фрилансер")
    FREELANCER,

    @Schema(description = "Администратор")
    ADMIN
}

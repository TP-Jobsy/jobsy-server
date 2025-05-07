package com.example.jobsyserver.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус проекта")
public enum ProjectStatus {
    @Schema(description = "Черновик")
    DRAFT,
    @Schema(description = "Открыт")
    OPEN,

    @Schema(description = "В работе")
    IN_PROGRESS,

    @Schema(description = "Завершён")
    COMPLETED
}

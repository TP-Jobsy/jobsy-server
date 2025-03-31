package com.example.jobsyserver.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус отклика на проект")
public enum ProjectApplicationStatus {
    @Schema(description = "Ожидает рассмотрения")
    PENDING,

    @Schema(description = "Одобрен")
    APPROVED,

    @Schema(description = "Отклонён")
    DECLINED
}

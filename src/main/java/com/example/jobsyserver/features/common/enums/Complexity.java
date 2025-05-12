package com.example.jobsyserver.features.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сложность проекта")
public enum Complexity {
    @Schema(description = "Лёгкий")
    EASY,

    @Schema(description = "Средний")
    MEDIUM,

    @Schema(description = "Сложный")
    HARD
}

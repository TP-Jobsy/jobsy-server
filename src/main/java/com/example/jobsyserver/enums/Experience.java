package com.example.jobsyserver.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Уровень опыта фрилансера")
public enum Experience {
    @Schema(description = "Начинающий")
    BEGINNER,

    @Schema(description = "Средний уровень")
    MIDDLE,

    @Schema(description = "Эксперт")
    EXPERT
}

package com.example.jobsyserver.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Продолжительность проекта")
public enum ProjectDuration {
    @Schema(description = "Менее 1 месяца")
    LESS_THAN_1_MONTH,

    @Schema(description = "От 1 до 3 месяцев")
    LESS_THAN_3_MONTHS,

    @Schema(description = "От 3 до 6 месяцев")
    LESS_THAN_6_MONTHS
}

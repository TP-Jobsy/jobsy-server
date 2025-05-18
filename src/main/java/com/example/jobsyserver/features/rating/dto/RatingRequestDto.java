package com.example.jobsyserver.features.rating.dto;

import com.example.jobsyserver.features.common.validation.annotation.ValidScore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO запроса на оценку проекта")
public class RatingRequestDto {

    @Schema(
            description = "Оценка проекта по шкале от 1 (минимум) до 5 (максимум)",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "4"
    )
    @NotNull(message = "Оценка не должна быть пустой")
    @ValidScore
    private Integer score;
}
package com.example.jobsyserver.features.rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO для отображения оценок по проекту")
public class RatingResponseDto {

    @Schema(description = "ID оценки", example = "42")
    private Long id;

    @Schema(description = "ID проекта, за который выставлена оценка", example = "1001")
    private Long projectId;

    @Schema(description = "ID фрилансера, который поставил оценку (если оценивал фрилансер)", example = "7")
    private Long raterFreelancerId;

    @Schema(description = "ID клиента, который поставил оценку (если оценивал клиент)", example = "3")
    private Long raterClientId;

    @Schema(description = "ID фрилансера, которому поставили оценку (если целевой профиль — фрилансер)", example = "7")
    private Long targetFreelancerId;

    @Schema(description = "ID клиента, которому поставили оценку (если целевой профиль — клиент)", example = "3")
    private Long targetClientId;

    @Schema(description = "Оценка по шкале от 1 до 5", example = "4")
    private Integer score;

    @Schema(description = "Время создания оценки", example = "2025-05-18T12:34:56")
    private LocalDateTime createdAt;
}
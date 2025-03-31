package com.example.jobsyserver.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO специализации")
public class SpecializationDto {

    @Schema(description = "ID специализации", example = "1")
    private Long id;

    @Schema(description = "ID категории, к которой относится специализация", example = "1")
    private Long categoryId;

    @Schema(description = "Название специализации", example = "Frontend Development")
    private String name;
}
package com.example.jobsyserver.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO категории")
public class CategoryDto {

    @Schema(description = "ID категории", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Web Development")
    private String name;
}
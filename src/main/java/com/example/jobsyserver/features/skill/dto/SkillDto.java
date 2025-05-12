package com.example.jobsyserver.features.skill.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO навыка")
public class SkillDto {

    @Schema(description = "ID навыка", example = "1")
    private Long id;

    @Schema(description = "Название навыка", example = "JavaScript")
    private String name;
}
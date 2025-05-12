package com.example.jobsyserver.features.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO с информацией о поданной заявке")
public class ProjectApplicationDto extends ProjectApplicationBasicDto {

    @Schema(description = "ID заявки", example = "100")
    private Long id;

    @Schema(description = "Дата создания заявки", example = "2024-04-18T14:00:00")
    private LocalDateTime createdAt;
}

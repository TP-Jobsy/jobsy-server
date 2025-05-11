package com.example.jobsyserver.features.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO для отправки заявки на участие в проекте")
public class ProjectApplicationRequestDto extends ProjectApplicationBasicDto {
}

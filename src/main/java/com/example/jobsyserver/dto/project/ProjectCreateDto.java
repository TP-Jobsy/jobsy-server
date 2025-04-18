package com.example.jobsyserver.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
@Schema(description = "DTO создания нового проекта")
public class ProjectCreateDto extends ProjectBasicDto {
}

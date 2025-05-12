package com.example.jobsyserver.features.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO обновления информации проекта")
public class ProjectUpdateDto extends ProjectBasicDto {
}

package com.example.jobsyserver.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Schema(description = "DTO обновления информации проекта")
public class ProjectUpdateDto extends ProjectBasicDto {
}

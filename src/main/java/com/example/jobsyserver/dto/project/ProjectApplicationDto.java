package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.enums.ProjectApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Базовый класс с информацией о заявке на выполнение проекта")
public class ProjectApplicationDto {
    private Long id;
    private Long projectId;
    private Long freelancerId;
    private ProjectApplicationStatus status;
    private LocalDateTime createdAt;
}

package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.enums.ApplicationType;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Schema(description = "Базовый DTO заявки")
public class ProjectApplicationBasicDto {

    @NotNull(message = "ID проекта обязателен")
    @Schema(description = "ID проекта", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    protected Long projectId;

    @NotNull(message = "ID фрилансера обязателен")
    @Schema(description = "ID фрилансера", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    protected Long freelancerId;

    @Schema(description = "Тип заявки (RESPONSE или INVITATION)", example = "RESPONSE")
    protected ApplicationType applicationType;

    @Schema(description = "Статус заявки", example = "PENDING")
    protected ProjectApplicationStatus status;
}
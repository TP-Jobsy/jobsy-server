package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.enums.ProjectApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для отправки заявки на работу над проектом")
public class ProjectApplicationRequestDto {
    @NotNull
    private Long projectId;

    @NotNull
    private Long freelancerId;

    private ProjectApplicationStatus status;
}

package com.example.jobsyserver.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Детальная информация о проекте вместе с откликами и приглашениями")
public class ProjectDetailDto {

    @Schema(description = "Основные данные проекта")
    private ProjectDto project;

    @Schema(description = "Список откликов фрилансеров на этот проект")
    private List<ProjectApplicationDto> responses;

    @Schema(description = "Список приглашений, отправленных клиентом фрилансерам")
    private List<ProjectApplicationDto> invitations;
}

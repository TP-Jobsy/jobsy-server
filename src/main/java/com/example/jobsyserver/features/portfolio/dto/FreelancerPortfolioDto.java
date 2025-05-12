package com.example.jobsyserver.features.portfolio.dto;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Элемент портфолио фрилансера")
public class FreelancerPortfolioDto {
    @Schema(description = "Идентификатор портфолио", example = "1")
    private Long id;

    @Schema(description = "Идентификатор фрилансера-владельца", example = "42")
    private Long freelancerId;

    @Schema(description = "Заголовок проекта", example = "Проект A")
    private String title;

    @Schema(description = "Описание проекта", example = "Разработал фронтенд на React")
    private String description;

    @Schema(description = "Роль фрилансера", example = "Frontend Developer")
    private String roleInProject;

    @Schema(description = "Ссылка на проект", example = "https://github.com/example/projectA")
    private String projectLink;

    @Schema(description = "Список прикреплённых навыков (ID + имя)",
            implementation = SkillDto.class)
    @Size(max = 5, message = "Нельзя указывать более 5 навыков")
    private List<SkillDto> skills;

    @Schema(description = "Дата создания записи", example = "2025-05-02T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления", example = "2025-05-03T08:21:10")
    private LocalDateTime updatedAt;
}
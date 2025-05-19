package com.example.jobsyserver.features.project.dto;

import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.category.dto.CategoryDto;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.specialization.dto.SpecializationDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.common.enums.Complexity;
import com.example.jobsyserver.features.common.enums.PaymentType;
import com.example.jobsyserver.features.common.enums.ProjectDuration;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Базовая информация о проекте")
public class ProjectDto {

    @Schema(description = "Идентификатор проекта", example = "1001")
    private Long id;

    @Schema(description = "Название проекта", example = "Разработка сайта")
    private String title;

    @Schema(description = "Описание проекта")
    private String description;

    @Schema(description = "Сложность проекта")
    private Complexity complexity;

    @Schema(description = "Тип оплаты")
    private PaymentType paymentType;

    @Schema(description = "Фиксированная цена", example = "1200.00")
    private BigDecimal fixedPrice;

    @Schema(description = "Длительность проекта")
    private ProjectDuration duration;

    @Schema(description = "Статус проекта", example = "OPEN")
    private ProjectStatus status;

    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления")
    private LocalDateTime updatedAt;

    @Schema(description = "Категория проекта")
    private CategoryDto category;

    @Schema(description = "Специализация проекта")
    private SpecializationDto specialization;

    @Schema(description = "Навыки, необходимые для проекта")
    private List<SkillDto> skills;

    @Schema(description = "Информация о клиенте, создавшем проект")
    private ClientProfileDto client;

    @Schema(description = "Исполнитель проекта (фрилансер), если назначен")
    private FreelancerProfileDto assignedFreelancer;

    @Schema(description = "Клиент нажал \"Завершил\"")
    @Builder.Default
    private boolean clientCompleted = false;

    @Schema(description = "Фрилансер нажал \"Завершил\"")
    @Builder.Default
    private boolean freelancerCompleted = false;
}

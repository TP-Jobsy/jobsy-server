package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.dto.client.ClientProfileDto;
import com.example.jobsyserver.dto.common.CategoryDto;
import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.enums.Complexity;
import com.example.jobsyserver.enums.PaymentType;
import com.example.jobsyserver.enums.ProjectDuration;
import com.example.jobsyserver.enums.ProjectStatus;
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

    @Schema(description = "Минимальная ставка фрилансеров", example = "500.00")
    private BigDecimal minRate;

    @Schema(description = "Максимальная ставка фрилансеров", example = "1500.00")
    private BigDecimal maxRate;

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
}

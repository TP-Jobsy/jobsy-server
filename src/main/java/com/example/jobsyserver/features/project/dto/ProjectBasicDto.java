package com.example.jobsyserver.features.project.dto;

import com.example.jobsyserver.features.category.dto.CategoryDto;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.specialization.dto.SpecializationDto;
import com.example.jobsyserver.features.common.enums.Complexity;
import com.example.jobsyserver.features.common.enums.PaymentType;
import com.example.jobsyserver.features.common.enums.ProjectDuration;
import com.example.jobsyserver.features.common.validation.group.Publish;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
@Schema(description = "Базовый DTO для проекта. Обязательные поля проверяются только при Publish")
public class ProjectBasicDto {

    @NotBlank(message = "Название проекта не может быть пустым", groups = Publish.class)
    @Size(max = 200, message = "Название проекта не должно превышать 200 символов")
    @Schema(description = "Название проекта", example = "Разработка сайта", maxLength = 200, requiredMode= RequiredMode.REQUIRED)
    protected String title;

    @NotBlank(message = "Описание проекта не может быть пустым", groups = Publish.class)
    @Schema(description = "Описание проекта", example = "Необходимо разработать веб-приложение на Spring", requiredMode= RequiredMode.REQUIRED)
    protected String description;

    @NotNull(message = "Сложность проекта обязательна", groups = Publish.class)
    @Schema(description = "Сложность проекта", example = "MEDIUM", requiredMode= RequiredMode.REQUIRED)
    protected Complexity complexity;

    @NotNull(message = "Тип оплаты обязателен", groups = Publish.class)
    @Schema(description = "Тип оплаты", example = "FIXED", requiredMode= RequiredMode.REQUIRED)
    protected PaymentType paymentType;

    @NotNull(message = "Фиксированная оплата обязательна", groups = Publish.class)
    @DecimalMin(value = "0.01", message = "Фиксированная оплата должна быть не менее 0.01")
    @Schema(description = "Фиксированная оплата", example = "1000.00", minimum = "0.01", requiredMode= RequiredMode.REQUIRED)
    protected BigDecimal fixedPrice;

    @NotNull(message = "Длительность проекта обязательна", groups = Publish.class)
    @Schema(description = "Длительность проекта", example = "LESS_THAN_1_MONTH", requiredMode= RequiredMode.REQUIRED)
    protected ProjectDuration duration;

    @NotNull(message = "Категория проекта обязательна", groups = Publish.class)
    @Schema(description = "Категория проекта", requiredMode= RequiredMode.REQUIRED)
    private CategoryDto category;

    @NotNull(message = "Специализация проекта обязательна", groups = Publish.class)
    @Schema(description = "Специализация проекта", requiredMode= RequiredMode.REQUIRED)
    private SpecializationDto specialization;

    @Schema(description = "Навыки, необходимые для проекта")
    @Size(max = 5, message = "Нельзя указывать более 5 навыков на проект", groups = Publish.class)
    private List<SkillDto> skills;
}

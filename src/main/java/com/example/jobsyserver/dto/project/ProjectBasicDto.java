package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.dto.common.CategoryDto;
import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.enums.Complexity;
import com.example.jobsyserver.enums.PaymentType;
import com.example.jobsyserver.enums.ProjectDuration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Базовый DTO для информации о проекте")
public class ProjectBasicDto {

    @NotBlank
    @Size(max = 200)
    @Schema(description = "Название проекта", example = "Разработка сайта", maxLength = 200)
    protected String title;

    @Schema(description = "Описание проекта", example = "Необходимо разработать веб-приложение на спринге")
    protected String description;

    @Schema(description = "Сложность проекта", example = "MEDIUM")
    protected Complexity complexity;

    @Schema(description = "Тип оплаты", example = "FIXED")
    protected PaymentType paymentType;

    @DecimalMin("0.01")
    @Schema(description = "Фиксированная оплата", example = "1000.00", minimum = "0.01")
    protected BigDecimal fixedPrice;

    @Schema(description = "Длительность проекта", example = "LESS_THAN_1_MONTH")
    protected ProjectDuration duration;

    @Schema(description = "Категория проекта")
    private CategoryDto category;

    @Schema(description = "Специализация проекта")
    private SpecializationDto specialization;

    @Schema(description = "Навыки, необходимые для проекта")
    @Size(max = 5, message = "Нельзя указывать более 5 навыков на проект")
    private List<SkillDto> skills;
}

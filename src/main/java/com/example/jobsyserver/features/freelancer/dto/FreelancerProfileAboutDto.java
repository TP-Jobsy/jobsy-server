package com.example.jobsyserver.features.freelancer.dto;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.common.enums.Experience;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Данные раздела «О себе» профиля фрилансера")
public class FreelancerProfileAboutDto {

    @Schema(description = "Идентификатор категории", example = "1")
    private Long categoryId;

    @Schema(description = "Название категории", example = "Веб-разработка")
    private String categoryName;

    @Schema(description = "Идентификатор специализации", example = "1")
    private Long specializationId;

    @Schema(description = "Название специализации", example = "Front-end")
    private String specializationName;

    @Schema(description = "Уровень опыта работы", example = "Senior")
    private Experience experienceLevel;

    @Size(max = 1000, message = "Поле \"О себе\" не должно превышать 1000 символов")
    @Schema(description = "Текст о себе", example = "Опытный разработчик с 5-летним стажем...")
    private String aboutMe;

    @Schema(description = "Список навыков фрилансера")
    @Size(max = 5, message = "Нельзя указывать более 5 навыков")
    private List<SkillDto> skills;
}
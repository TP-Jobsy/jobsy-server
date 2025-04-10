package com.example.jobsyserver.dto.freelancer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "Базовые поля для профиля фрилансера")
public abstract class FreelancerProfileBaseDto {

    @Schema(description = "Уровень опыта", example = "Senior")
    private String experienceLevel;

    @Size(max = 1000, message = "Поле 'О себе' не должно превышать 1000 символов")
    @Schema(description = "Информация 'О себе'", example = "Опытный Java-разработчик с 5-летним стажем...")
    private String aboutMe;

    @NotBlank(message = "Ссылка не может быть пустой")
    @URL(message = "Некорректный URL")
    @Schema(description = "Ссылка для связи или портфолио", example = "https://linkedin.com/in/username")
    private String contactLink;

    @Schema(description = "Идентификатор категории", example = "1")
    private Long categoryId;

    @Schema(description = "Идентификатор специализации", example = "1")
    private Long specializationId;

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;
}

package com.example.jobsyserver.dto.client;

import com.example.jobsyserver.dto.user.PublicUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Основные данные профиля заказчика")
public class ClientProfileBasicDto extends PublicUserDto {

    @Size(max = 100, message = "Название компании не должен превышать 100 символов")
    @Schema(description = "Название компании", example = "ООО Ромашка")
    private String companyName;

    @Size(max = 100, message = "Должность не должен превышать 100 символов")
    @Schema(description = "Должность", example = "Менеджер проектов")
    private String position;

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;
}
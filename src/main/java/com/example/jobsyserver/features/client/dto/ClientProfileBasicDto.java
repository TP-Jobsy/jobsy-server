package com.example.jobsyserver.features.client.dto;

import com.example.jobsyserver.features.common.validation.annotation.CityInCountry;
import com.example.jobsyserver.features.common.validation.annotation.ValidCountry;
import com.example.jobsyserver.features.user.dto.PublicUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@CityInCountry
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Основные данные профиля заказчика")
public class ClientProfileBasicDto extends PublicUserDto {

    @Size(max = 100, message = "Название компании не должен превышать 100 символов")
    @Schema(description = "Название компании", example = "ООО Ромашка")
    private String companyName;

    @Size(max = 100, message = "Должность не должен превышать 100 символов")
    @Schema(description = "Должность", example = "Менеджер проектов")
    private String position;

    @ValidCountry
    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;
}
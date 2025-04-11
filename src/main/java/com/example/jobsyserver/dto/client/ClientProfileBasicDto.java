package com.example.jobsyserver.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Основные данные профиля заказчика")
public class ClientProfileBasicDto {

    @Schema(description = "Название компании", example = "Acme Corp")
    private String companyName;

    @Schema(description = "Должность", example = "Менеджер проектов")
    private String position;

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;
}
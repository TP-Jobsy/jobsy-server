package com.example.jobsyserver.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о сфере деятельности заказчика")
public class ClientProfileFieldDto {

    @Schema(description = "Описание сферы деятельности компании", example = "Информационные технологии")
    private String fieldDescription;
}
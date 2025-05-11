package com.example.jobsyserver.features.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Информация о сфере деятельности заказчика")
public class ClientProfileFieldDto {

    @Size(max = 800, message = "Сфера деятельности не должен превышать 800 символов")
    @Schema(description = "Описание сферы деятельности компании", example = "Информационные технологии")
    private String fieldDescription;
}
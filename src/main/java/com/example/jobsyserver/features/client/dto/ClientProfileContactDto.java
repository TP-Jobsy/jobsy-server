package com.example.jobsyserver.features.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "Контактные данные профиля заказчика")
public class ClientProfileContactDto {

    @URL(protocol = "http", regexp = "^(http|https)://.*$", message = "Должен быть корректный HTTP/HTTPS URL")
    @Schema(description = "Ссылка для связи или на сайт компании", example = "http://acme-corp.example.com")
    private String contactLink;
}
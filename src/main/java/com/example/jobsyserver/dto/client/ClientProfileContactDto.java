package com.example.jobsyserver.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Контактные данные профиля заказчика")
public class ClientProfileContactDto {

    @Schema(description = "Ссылка для связи или на сайт компании", example = "http://acme-corp.example.com")
    private String contactLink;
}
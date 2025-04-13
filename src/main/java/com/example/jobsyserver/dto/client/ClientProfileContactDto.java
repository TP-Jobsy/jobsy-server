package com.example.jobsyserver.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "Контактные данные профиля заказчика")
public class ClientProfileContactDto {

    @NotBlank(message = "Ссылка не может быть пустой")
    @URL(message = "Некорректный URL")
    @Schema(description = "Ссылка для связи или на сайт компании", example = "http://acme-corp.example.com")
    private String contactLink;
}
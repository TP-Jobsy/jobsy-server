package com.example.jobsyserver.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные пользователя для профиля заказчика")
public class ClientProfileUserDto {

    @Schema(description = "Имя заказчика", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия заказчика", example = "Иванов")
    private String lastName;

    @Schema(description = "Номер телефона заказчика", example = "+79991234567")
    private String phone;

    @Schema(description = "Email заказчика (только для отображения)", example = "ivan.ivanov@example.com")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;
}
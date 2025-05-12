package com.example.jobsyserver.features.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Публичный dto пользователя, содержащий только имя, фамилию, почту и номер телефона")
public class PublicUserDto {

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Email пользователя", example = "ivan.ivanov@example.com")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;

    @Schema(description = "Номер телефона, 10 цифр, начиная с 7", example = "79991234567")
    private String phone;
}
package com.example.jobsyserver.dto.freelancer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные пользователя для профиля фрилансера")
public class FreelancerProfileUserDto {

    @Schema(description = "Имя фрилансера", example = "Петр")
    private String firstName;

    @Schema(description = "Фамилия фрилансера", example = "Петров")
    private String lastName;

    @Schema(description = "Номер телефона фрилансера", example = "+79991234567")
    private String phone;

    @Schema(description = "Email фрилансера (только для отображения)", example = "petr.petrov@example.com")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String email;
}
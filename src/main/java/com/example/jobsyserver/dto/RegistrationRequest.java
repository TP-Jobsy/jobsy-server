package com.example.jobsyserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Данные для регистрации нового пользователя")
public class RegistrationRequest {

    @NotBlank
    @Schema(description = "Электронная почта пользователя", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "SecretPassword123!")
    private String password;

    @NotBlank
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @NotBlank
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "Телефон пользователя", example = "+79991234567")
    private String phone;

    @NotBlank
    @Schema(description = "Роль пользователя", allowableValues = {"client", "freelancer"}, example = "client")
    private String role;

    @NotNull(message = "Дата рождения обязательна")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @Schema(description = "Дата рождения пользователя в формате dd.MM.yyyy. Регистрация доступна только для пользователей старше 18 лет",
            example = "01.01.2000")
    private LocalDate dateBirth;
}

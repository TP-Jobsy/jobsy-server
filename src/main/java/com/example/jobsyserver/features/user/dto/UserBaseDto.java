package com.example.jobsyserver.features.user.dto;

import com.example.jobsyserver.features.common.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Базовый класс для передачи информации о пользователе")
public abstract class UserBaseDto {

    @Schema(description = "Идентификатор пользователя", example = "2")
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    protected String email;

    @NotBlank(message = "Имя не может быть пустым")
    @Schema(description = "Имя пользователя", example = "Иван")
    protected String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    protected String lastName;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "7\\d{10}", message = "Телефон должен начинаться с '7' и содержать ровно 10 цифр")
    @Schema(description = "Номер телефона, 10 цифр, начиная с 7", example = "79991234567")
    protected String phone;

    @Schema(description = "Роль пользователя", example = "client")
    protected UserRole role;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    @Schema(description = "Дата рождения", example = "2000-01-01")
    protected LocalDate dateBirth;
}


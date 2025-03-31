package com.example.jobsyserver.dto.user;

import com.example.jobsyserver.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Базовый класс для передачи информации о пользователе")
public abstract class UserBaseDto {

    @Schema(description = "E-mail пользователя", example = "user@example.com")
    protected String email;

    @Schema(description = "Имя пользователя", example = "Иван")
    protected String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    protected String lastName;

    @Schema(description = "Номер телефона", example = "+79991234567")
    protected String phone;

    @Schema(description = "Роль пользователя", example = "client")
    protected UserRole role;

    @Schema(description = "Дата рождения", example = "2000-01-01")
    protected LocalDate dateBirth;
}


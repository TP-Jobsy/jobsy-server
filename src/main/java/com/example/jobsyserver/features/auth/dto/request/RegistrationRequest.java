package com.example.jobsyserver.features.auth.dto.request;

import com.example.jobsyserver.features.user.dto.UserBaseDto;
import com.example.jobsyserver.features.common.validation.Adult;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Данные для регистрации нового пользователя")
public class RegistrationRequest extends UserBaseDto {

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Schema(description = "Пароль пользователя", example = "SecretPassword123!")
    private String password;

    @Override
    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    @Adult(value = 18, message = "Регистрация доступна только для пользователей старше 18 лет")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @Schema(
            description = "Дата рождения пользователя в формате dd.MM.yyyy. Регистрация доступна только для пользователей старше 18 лет",
            example = "01.01.2000"
    )
    public LocalDate getDateBirth() {
        return super.getDateBirth();
    }
}
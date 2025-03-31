package com.example.jobsyserver.dto.request;

import com.example.jobsyserver.dto.user.UserBaseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Данные для регистрации нового пользователя")
public class RegistrationRequest extends UserBaseDto {

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "SecretPassword123!")
    private String password;

    @Override
    @NotNull(message = "Дата рождения обязательна")
    @JsonFormat(pattern = "dd.MM.yyyy")
    @Schema(description = "Дата рождения пользователя в формате dd.MM.yyyy. Регистрация доступна только для пользователей старше 18 лет",
            example = "01.01.2000")
    public LocalDate getDateBirth() {
        return super.getDateBirth();
    }
}
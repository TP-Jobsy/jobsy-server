package com.example.jobsyserver.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Schema(description = "DTO пользователя для отображения")
public class UserDto extends UserBaseDto {

    @Schema(description = "Флаг подтверждения email", example = "true")
    private Boolean isVerified;

    @Schema(description = "Активность аккаунта", example = "true")
    private Boolean isActive;

    @Schema(description = "Дата создания аккаунта", example = "2025-05-01T12:34:56")
    private LocalDateTime createdAt;
}

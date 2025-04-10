package com.example.jobsyserver.dto.freelancer;

import com.example.jobsyserver.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Данные профиля фрилансера для отображения")
public class FreelancerProfileDto extends FreelancerProfileBaseDto {

    @Schema(description = "Идентификатор профиля фрилансера", example = "1")
    private Long id;

    @Schema(description = "Дата и время создания профиля", example = "2025-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время последнего обновления профиля", example = "2025-01-05T12:34:56")
    private LocalDateTime updatedAt;

    @Schema(description = "Информация о пользователе (общие данные)", implementation = UserDto.class)
    private UserDto user;
}

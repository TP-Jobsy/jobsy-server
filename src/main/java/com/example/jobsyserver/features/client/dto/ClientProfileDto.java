package com.example.jobsyserver.features.client.dto;

import com.example.jobsyserver.features.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Полные данные профиля заказчика для отображения")
public class ClientProfileDto {

    @Schema(description = "Идентификатор профиля заказчика", example = "1")
    private Long id;

    @Schema(description = "Основные данные компании")
    private ClientProfileBasicDto basic;

    @Schema(description = "Контактные данные")
    private ClientProfileContactDto contact;

    @Schema(description = "Информация о сфере деятельности")
    private ClientProfileFieldDto field;

    @Schema(description = "Данные пользователя", implementation = UserDto.class)
    private UserDto user;

    @Schema(description = "Дата создания профиля", example = "2024-03-30T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления профиля", example = "2024-03-30T12:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "url аватарки клиента")
    private String avatarUrl;

    @Schema(description = "Средний рейтинг клиента", example = "4.2")
    private Double averageRating;

    @Schema(description = "Количество полученных оценок", example = "5")
    private Integer ratingCount;
}
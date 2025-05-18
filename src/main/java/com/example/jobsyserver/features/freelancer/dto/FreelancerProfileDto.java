package com.example.jobsyserver.features.freelancer.dto;

import com.example.jobsyserver.features.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Полные данные профиля фрилансера для отображения")
public class FreelancerProfileDto {

    @Schema(description = "Идентификатор профиля фрилансера", example = "1")
    private Long id;

    @Schema(description = "Дополнительные данные профиля фрилансера")
    private FreelancerProfileAboutDto about;

    @Schema(description = "Основные данные профиля фрилансера")
    private FreelancerProfileBasicDto basic;

    @Schema(description = "Контактные данные профиля фрилансера")
    private FreelancerProfileContactDto contact;

    @Schema(description = "Данные пользователя (фрилансера)", implementation = UserDto.class)
    private UserDto user;

    @Schema(description = "Дата создания профиля", example = "2024-03-30T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления профиля", example = "2024-03-30T12:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "url аватарки фрилансера")
    private String avatarUrl;

    @Schema(description = "Средний рейтинг фрилансера", example = "4.5")
    private Double averageRating;

    @Schema(description = "Количество полученных оценок", example = "12")
    private Integer ratingCount;
}
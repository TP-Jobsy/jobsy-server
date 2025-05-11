package com.example.jobsyserver.features.freelancer.dto;

import com.example.jobsyserver.features.user.dto.PublicUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Основные данные профиля фрилансера")
public class FreelancerProfileBasicDto extends PublicUserDto {

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;

}
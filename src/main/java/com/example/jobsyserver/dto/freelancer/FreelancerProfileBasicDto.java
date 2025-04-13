package com.example.jobsyserver.dto.freelancer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Основные данные профиля фрилансера")
public class FreelancerProfileBasicDto {

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;

}
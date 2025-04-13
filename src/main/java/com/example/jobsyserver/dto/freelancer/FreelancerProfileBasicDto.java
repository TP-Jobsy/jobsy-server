package com.example.jobsyserver.dto.freelancer;

import com.example.jobsyserver.dto.user.PublicUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Основные данные профиля фрилансера")
public class FreelancerProfileBasicDto extends PublicUserDto {

    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;

}
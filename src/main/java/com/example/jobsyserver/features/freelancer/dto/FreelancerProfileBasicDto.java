package com.example.jobsyserver.features.freelancer.dto;

import com.example.jobsyserver.features.common.validation.annotation.CityInCountry;
import com.example.jobsyserver.features.common.validation.annotation.ValidCountry;
import com.example.jobsyserver.features.user.dto.PublicUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@CityInCountry
@Schema(description = "Основные данные профиля фрилансера")
public class FreelancerProfileBasicDto extends PublicUserDto {

    @ValidCountry
    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Schema(description = "Город", example = "Москва")
    private String city;

}
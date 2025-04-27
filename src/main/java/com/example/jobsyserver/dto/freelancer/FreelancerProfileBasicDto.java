package com.example.jobsyserver.dto.freelancer;

import com.example.jobsyserver.dto.user.PublicUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Основные данные профиля фрилансера")
public class FreelancerProfileBasicDto extends PublicUserDto {

    @NotBlank(message = "Страна не может быть пустым")
    @Schema(description = "Страна", example = "Россия")
    private String country;

    @NotBlank(message = "Город не может быть пустым")
    @Schema(description = "Город", example = "Москва")
    private String city;

}
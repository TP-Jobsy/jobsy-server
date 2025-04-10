package com.example.jobsyserver.dto.freelancer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Данные для обновления профиля фрилансера")
public class FreelancerProfileUpdateDto extends FreelancerProfileBaseDto {

    @Schema(description = "Имя фрилансера", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия фрилансера", example = "Иванов")
    private String lastName;

    @Schema(description = "Номер телефона фрилансера", example = "+79991234567")
    private String phone;
}

package com.example.jobsyserver.features.freelancer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "Контактные данные фрилансера")
public class FreelancerProfileContactDto {

    @URL(protocol = "http", regexp = "^(http|https)://.*$", message = "Должен быть корректный HTTP/HTTPS URL")
    @Schema(description = "Ссылка для связи или портфолио", example = "https://linkedin.com/in/username")
    private String contactLink;
}
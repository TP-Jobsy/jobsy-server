package com.example.jobsyserver.dto.freelancer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "Контактные данные фрилансера")
public class FreelancerProfileContactDto {

    @NotBlank(message = "Ссылка не может быть пустой")
    @URL(protocol = "http", regexp = "^(http|https)://.*$", message = "Должен быть корректный HTTP/HTTPS URL")
    @Schema(description = "Ссылка для связи или портфолио", example = "https://linkedin.com/in/username")
    private String contactLink;
}
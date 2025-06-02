package com.example.jobsyserver.features.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для создания элемента портфолио")
public class FreelancerPortfolioCreateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String roleInProject;

    @NotBlank
    @URL(protocol = "https", message  = "Ссылка должна быть HTTPS")
    private String projectLink;

    private List<Long> skillIds;
}

package com.example.jobsyserver.dto.portfolio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    private String projectLink;
    private List<Long> skillIds;
}

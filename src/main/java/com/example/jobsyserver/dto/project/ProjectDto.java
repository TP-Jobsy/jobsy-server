package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.enums.Complexity;
import com.example.jobsyserver.enums.PaymentType;
import com.example.jobsyserver.enums.ProjectDuration;
import com.example.jobsyserver.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Базовый класс информации о проекте")
public class ProjectDto {

    private Long id;
    private String title;
    private String description;
    private Complexity complexity;
    private PaymentType paymentType;
    private BigDecimal minRate;
    private BigDecimal maxRate;
    private BigDecimal fixedPrice;
    private ProjectDuration duration;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.example.jobsyserver.dto.project;

import com.example.jobsyserver.enums.Complexity;
import com.example.jobsyserver.enums.PaymentType;
import com.example.jobsyserver.enums.ProjectDuration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO обновления информации проекта")
public class ProjectUpdateDto {

    @NotBlank
    @Size(max = 200)
    private String title;

    private String description;

    private Complexity complexity;

    private PaymentType paymentType;

    @DecimalMin("0.01")
    private BigDecimal minRate;

    @DecimalMin("0.01")
    private BigDecimal maxRate;

    @DecimalMin("0.01")
    private BigDecimal fixedPrice;

    private ProjectDuration duration;
}

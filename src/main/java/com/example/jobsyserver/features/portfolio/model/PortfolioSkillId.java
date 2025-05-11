package com.example.jobsyserver.features.portfolio.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PortfolioSkillId implements Serializable {
    private Long portfolioId;
    private Long skillId;
}

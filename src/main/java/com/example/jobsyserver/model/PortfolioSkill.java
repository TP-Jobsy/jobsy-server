package com.example.jobsyserver.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "portfolio_skills")
public class PortfolioSkill {

    @EmbeddedId
    private PortfolioSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("portfolioId")
    @JoinColumn(name = "portfolio_id", nullable = false)
    private FreelancerPortfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
}
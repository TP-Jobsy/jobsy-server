package com.example.jobsyserver.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "freelancer_skills")
public class FreelancerSkill {

    @EmbeddedId
    private FreelancerSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("freelancerId")
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerProfile freelancerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

}
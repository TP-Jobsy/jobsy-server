package com.example.jobsyserver.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FreelancerSkillId implements Serializable {

    private Long freelancerId;
    private Long skillId;
}

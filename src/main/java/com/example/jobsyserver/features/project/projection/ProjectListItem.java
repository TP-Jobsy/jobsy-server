package com.example.jobsyserver.features.project.projection;

import com.example.jobsyserver.features.common.enums.Complexity;
import com.example.jobsyserver.features.common.enums.ProjectDuration;
import com.example.jobsyserver.features.common.enums.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProjectListItem {
    Long getId();

    String getTitle();

    BigDecimal getFixedPrice();

    Complexity getProjectComplexity();

    ProjectDuration getProjectDuration();

    ProjectStatus getStatus();

    LocalDateTime getCreatedAt();

    String getClientCompanyName();

    String getClientCity();

    String getClientCountry();

    String getAssignedFreelancerFirstName();

    String getAssignedFreelancerLastName();
}

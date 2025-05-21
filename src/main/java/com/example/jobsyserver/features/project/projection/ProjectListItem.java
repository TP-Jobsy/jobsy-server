package com.example.jobsyserver.features.project.projection;

import java.math.BigDecimal;

public interface ProjectListItem {
    Long getId();

    String getTitle();

    BigDecimal getFixedPrice();

    String getClientCompanyName();

    String getAssignedFreelancerFirstName();

    String getAssignedFreelancerLastName();
}

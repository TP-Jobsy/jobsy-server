package com.example.jobsyserver.features.project.projection;

import java.math.BigDecimal;

public interface ProjectListItem {
    Long getId();
    String getTitle();
    BigDecimal getFixedPrice();

    ClientInfo getClient();
    FreelancerInfo getAssignedFreelancer();

    interface ClientInfo {
        String getCompanyName();
    }
    interface FreelancerInfo {
        String getFirstName();
        String getLastName();
    }
}

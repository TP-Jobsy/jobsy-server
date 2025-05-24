package com.example.jobsyserver.features.portfolio.projection;

import java.time.LocalDateTime;

public interface PortfolioAdminListItem {
    Long getId();

    String getTitle();

    LocalDateTime getCreatedAt();

    String getFirstName();

    String getLastName();
}

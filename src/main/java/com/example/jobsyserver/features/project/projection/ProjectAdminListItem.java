package com.example.jobsyserver.features.project.projection;

import java.time.LocalDateTime;

public interface ProjectAdminListItem {
    Long getId();

    String getTitle();

    LocalDateTime getCreatedAt();

    String getStatus();

    String getClientFirstName();

    String getClientLastName();
}

package com.example.jobsyserver.features.auth.service;

import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.user.model.User;

public interface SecurityService {
    String getCurrentUserEmail();
    User getCurrentUser();
    Project getProjectReference(Long projectId);
    Long getCurrentClientProfileId();
    Long getCurrentFreelancerProfileId();
}

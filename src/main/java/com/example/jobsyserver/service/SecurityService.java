package com.example.jobsyserver.service;

import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.User;

public interface SecurityService {
    String getCurrentUserEmail();
    User getCurrentUser();
    Project getProjectReference(Long projectId);
    Long getCurrentClientProfileId();
    Long getCurrentFreelancerProfileId();
}

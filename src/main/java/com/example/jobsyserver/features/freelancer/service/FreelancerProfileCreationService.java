package com.example.jobsyserver.features.freelancer.service;

import com.example.jobsyserver.features.auth.event.UserVerifiedEvent;

public interface FreelancerProfileCreationService {
    void handleUserVerified(UserVerifiedEvent event);
}

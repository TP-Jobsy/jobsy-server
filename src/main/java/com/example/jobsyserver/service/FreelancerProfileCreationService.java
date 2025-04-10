package com.example.jobsyserver.service;

import com.example.jobsyserver.event.UserVerifiedEvent;

public interface FreelancerProfileCreationService {
    void handleUserVerified(UserVerifiedEvent event);
}

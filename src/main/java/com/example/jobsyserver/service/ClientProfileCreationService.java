package com.example.jobsyserver.service;

import com.example.jobsyserver.event.UserVerifiedEvent;

public interface ClientProfileCreationService {
    void handleUserVerified(UserVerifiedEvent event);
}
package com.example.jobsyserver.features.client.service;

import com.example.jobsyserver.features.auth.event.UserVerifiedEvent;

public interface ClientProfileCreationService {
    void handleUserVerified(UserVerifiedEvent event);
}
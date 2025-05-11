package com.example.jobsyserver.features.auth.service;

import com.example.jobsyserver.features.auth.dto.request.RegistrationRequest;
import com.example.jobsyserver.features.auth.dto.response.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse register(RegistrationRequest request);
}

package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.request.RegistrationRequest;
import com.example.jobsyserver.dto.response.RegistrationResponse;

public interface RegistrationService {
    RegistrationResponse register(RegistrationRequest request);
}

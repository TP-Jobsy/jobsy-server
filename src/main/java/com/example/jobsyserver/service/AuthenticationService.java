package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.request.AuthenticationRequest;
import com.example.jobsyserver.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
}

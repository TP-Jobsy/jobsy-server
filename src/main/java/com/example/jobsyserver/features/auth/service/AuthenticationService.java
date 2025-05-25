package com.example.jobsyserver.features.auth.service;

import com.example.jobsyserver.features.auth.dto.request.AuthenticationRequest;
import com.example.jobsyserver.features.auth.dto.request.TokenRefreshRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.dto.response.TokenRefreshResponse;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);

    DefaultResponse logout();
}

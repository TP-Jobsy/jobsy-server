package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.admin.dto.AdminLoginRequest;
import com.example.jobsyserver.features.admin.dto.ConfirmAdminLoginRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;

public interface AdminAuthService {
    DefaultResponse requestLoginCode(AdminLoginRequest request);
    AuthenticationResponse confirmLoginCode(ConfirmAdminLoginRequest request);
}

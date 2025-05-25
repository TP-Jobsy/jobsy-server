package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.admin.dto.AdminLoginRequest;
import com.example.jobsyserver.features.admin.dto.ConfirmAdminLoginRequest;
import com.example.jobsyserver.features.auth.dto.request.TokenRefreshRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.dto.response.TokenRefreshResponse;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.model.User;

import java.util.Optional;

public interface AdminAuthService {
    DefaultResponse requestLoginCode(AdminLoginRequest request);

    AuthenticationResponse confirmLoginCode(ConfirmAdminLoginRequest request);

    Optional<User> findByEmailAndRole(String email, UserRole role);

    TokenRefreshResponse refresh(TokenRefreshRequest request);
}

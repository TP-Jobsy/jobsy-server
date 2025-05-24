package com.example.jobsyserver.features.admin.dto;

import jakarta.validation.constraints.Email;

public record AdminLoginRequest(
        @Email String email
) {}

package com.example.jobsyserver.features.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConfirmAdminLoginRequest(
        @Email String email,
        @NotBlank String confirmationCode
) {}

package com.example.jobsyserver.features.auth.service;

public interface PasswordResetService {
    void initiatePasswordReset(String email);
    void confirmPasswordReset(String email, String resetCode, String newPassword);
}

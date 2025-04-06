package com.example.jobsyserver.service;

public interface PasswordResetService {
    void initiatePasswordReset(String email);
    void confirmPasswordReset(String email, String resetCode, String newPassword);
}

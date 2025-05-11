package com.example.jobsyserver.features.auth.service;

public interface EmailService {
    void sendConfirmationEmail(String email, String confirmationCode);
    void sendPasswordResetEmail(String email, String resetCode);
}

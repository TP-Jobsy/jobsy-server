package com.example.jobsyserver.service;

public interface EmailService {
    void sendConfirmationEmail(String email, String confirmationCode);
    void sendPasswordResetEmail(String email, String resetCode);
}

package com.example.jobsyserver.features.auth.service;

public interface ConfirmEmailService {
    void confirmEmail(String email, String confirmationCode);
    void resendConfirmationCode(String email);
}

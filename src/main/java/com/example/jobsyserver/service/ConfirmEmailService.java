package com.example.jobsyserver.service;

public interface ConfirmEmailService {
    void confirmEmail(String email, String confirmationCode);
}

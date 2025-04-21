package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class ProfileNotFoundException extends AbstractException {
    public ProfileNotFoundException(String profileType, String email) {
        super(profileType + " профиль для пользователя '" + email + "' не найден", HttpStatus.NOT_FOUND.value());
    }
}
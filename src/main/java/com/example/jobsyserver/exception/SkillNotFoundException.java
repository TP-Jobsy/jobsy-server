package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class SkillNotFoundException extends AbstractException {
    public SkillNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}

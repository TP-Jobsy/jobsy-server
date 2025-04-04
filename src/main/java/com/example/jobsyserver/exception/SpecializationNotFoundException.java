package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class SpecializationNotFoundException extends AbstractException {
    public SpecializationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}

package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class ProjectApplicationNotFoundException extends AbstractException {
    public ProjectApplicationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}

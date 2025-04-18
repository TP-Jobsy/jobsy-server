package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends AbstractException {
    public ProjectNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}

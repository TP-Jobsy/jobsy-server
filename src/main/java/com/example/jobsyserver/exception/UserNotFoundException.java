package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}

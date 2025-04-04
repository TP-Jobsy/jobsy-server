package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends AbstractException {
    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}

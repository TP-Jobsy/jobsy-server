package com.example.jobsyserver.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractException {
    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(String.format("%s с идентификатором '%s' не найден", resourceName, identifier),
                HttpStatus.NOT_FOUND.value());
    }

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s не найден", resourceName),
                HttpStatus.NOT_FOUND.value()
        );
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s с %s '%s' не найден", resourceName, fieldName, fieldValue),
                HttpStatus.NOT_FOUND.value()
        );
    }
}

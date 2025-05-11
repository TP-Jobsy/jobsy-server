package com.example.jobsyserver.features.common.exception;

import org.springframework.http.HttpStatus;

public class AvatarStorageException extends AbstractException {
    public AvatarStorageException(String message) {
        super(message, HttpStatus.BAD_GATEWAY.value());
    }
}

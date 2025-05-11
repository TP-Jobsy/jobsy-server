package com.example.jobsyserver.features.common.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestsException extends AbstractException {
    public TooManyRequestsException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS.value());
    }
}

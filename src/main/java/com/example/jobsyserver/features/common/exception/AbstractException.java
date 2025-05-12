package com.example.jobsyserver.features.common.exception;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
    private final int statusCode;

    protected AbstractException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

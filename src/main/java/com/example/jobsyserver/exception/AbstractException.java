package com.example.jobsyserver.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public AbstractException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}

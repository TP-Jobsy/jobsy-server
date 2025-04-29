package com.example.jobsyserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "confirmation")
public record ConfirmationProperties(
        int expirationMinutes,
        CleanupProperties cleanup,
        int codeLength
) {
    public record CleanupProperties(long interval, long passwordResetInterval) {}
}

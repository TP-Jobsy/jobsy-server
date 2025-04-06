package com.example.jobsyserver.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "spring.mail", ignoreUnknownFields = false)
public record MailConfigProperties(
        @NotBlank String host,
        int port,
        @NotBlank String username,
        @NotBlank String password,
        Map<String, String> properties
) {}

package com.example.jobsyserver.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "novita.api")
public record NovitaProperties(
        @NotBlank String url,
        @NotBlank String key,
        @NotBlank String defaultSystemPrompt,
        @NotBlank String model
) {}

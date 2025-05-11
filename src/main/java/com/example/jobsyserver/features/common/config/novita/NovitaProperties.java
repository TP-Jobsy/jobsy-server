package com.example.jobsyserver.features.common.config.novita;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "novita.api")
public record NovitaProperties(
        @NotBlank String url,
        @NotBlank String key,
        @NotBlank String defaultSystemPrompt,
        @NotBlank String model
) {}

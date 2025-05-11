package com.example.jobsyserver.features.common.config.validation;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation.api")
public record ValidationApiProperties(
        ServiceProps restCountries,
        ServiceProps countriesNow
) {
    public record ServiceProps(
            String baseUrl
    ) {}
}

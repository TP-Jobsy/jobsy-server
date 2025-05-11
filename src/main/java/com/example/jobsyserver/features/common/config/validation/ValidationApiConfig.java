package com.example.jobsyserver.features.common.config.validation;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ValidationApiProperties.class)
public class ValidationApiConfig {}

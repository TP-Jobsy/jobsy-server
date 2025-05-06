package com.example.jobsyserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3")
public record S3FeatureToggle(boolean enabled) {}

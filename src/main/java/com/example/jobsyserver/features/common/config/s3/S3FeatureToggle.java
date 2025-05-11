package com.example.jobsyserver.features.common.config.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3")
public record S3FeatureToggle(boolean enabled) {}

package com.example.jobsyserver.features.common.config.admin;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "admin.auth.test")
public record AdminTestAuthProperties(boolean enabled, String email, String code) {
}

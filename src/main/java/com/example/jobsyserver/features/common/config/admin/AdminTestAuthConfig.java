package com.example.jobsyserver.features.common.config.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AdminTestAuthProperties.class)
@RequiredArgsConstructor
public class AdminTestAuthConfig {
}

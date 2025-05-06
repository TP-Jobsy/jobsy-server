package com.example.jobsyserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "skill")
public record SkillProperties(int popularLimit) {
}

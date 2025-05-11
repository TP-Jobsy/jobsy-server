package com.example.jobsyserver.features.common.config.skill;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "skill")
public record SkillProperties(int popularLimit) {
}

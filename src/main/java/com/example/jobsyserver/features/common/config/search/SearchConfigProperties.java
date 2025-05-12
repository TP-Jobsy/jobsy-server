package com.example.jobsyserver.features.common.config.search;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "search")
public record SearchConfigProperties(int maxSkills) {}

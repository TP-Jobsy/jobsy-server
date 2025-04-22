package com.example.jobsyserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "search")
public record SearchConfigProperties(int maxSkills) {}

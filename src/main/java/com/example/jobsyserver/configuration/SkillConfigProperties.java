package com.example.jobsyserver.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SkillProperties.class)
@RequiredArgsConstructor
public class SkillConfigProperties {
}

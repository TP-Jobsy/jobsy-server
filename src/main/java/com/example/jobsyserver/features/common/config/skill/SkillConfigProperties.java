package com.example.jobsyserver.features.common.config.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SkillProperties.class)
@RequiredArgsConstructor
public class SkillConfigProperties {
}

package com.example.jobsyserver.features.common.config.search;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SearchConfigProperties.class)
@RequiredArgsConstructor
public class SearchConfig {
}

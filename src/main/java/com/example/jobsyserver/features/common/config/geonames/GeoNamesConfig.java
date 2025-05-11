package com.example.jobsyserver.features.common.config.geonames;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GeoNamesProperties.class)
@RequiredArgsConstructor
public class GeoNamesConfig {}

package com.example.jobsyserver.features.common.config.geonames;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "geonames.api")
public record GeoNamesProperties(String baseUrl, String username) {}
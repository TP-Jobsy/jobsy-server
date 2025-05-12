package com.example.jobsyserver.features.common.validation.config;

import com.example.jobsyserver.features.common.config.geonames.GeoNamesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class GeoNamesWebClientConfig {
    private final GeoNamesProperties props;

    @Bean("geoNamesClient")
    public WebClient geoNamesClient(WebClient.Builder builder) {
        return builder
                .baseUrl(props.baseUrl())
                .build();
    }
}

package com.example.jobsyserver.features.common.validation.config;

import com.example.jobsyserver.features.common.config.validation.ValidationApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ValidationApiProperties props;

    @Bean("restCountriesClient")
    public WebClient restCountriesClient(WebClient.Builder builder) {
        return builder.baseUrl(props.restCountries().baseUrl()).build();
    }

    @Bean("countriesNowClient")
    public WebClient countriesNowClient(WebClient.Builder builder) {
        return builder.baseUrl(props.countriesNow().baseUrl()).build();
    }
}

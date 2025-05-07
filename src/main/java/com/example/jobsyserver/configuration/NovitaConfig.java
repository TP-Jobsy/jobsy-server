package com.example.jobsyserver.configuration;

import org.apache.http.HttpHeaders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(NovitaProperties.class)
public class NovitaConfig {
    @Bean
    public WebClient novitaWebClient(NovitaProperties props) {
        return WebClient.builder()
                .baseUrl(props.url())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.key())
                .build();
    }
}

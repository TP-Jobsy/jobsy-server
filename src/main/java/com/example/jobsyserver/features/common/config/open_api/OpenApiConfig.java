package com.example.jobsyserver.features.common.config.open_api;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenApiConfig {

    @Value("classpath:swagger.yaml")
    private Resource swaggerResource;

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        try (InputStream is = swaggerResource.getInputStream()) {
            String yamlContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new OpenAPIV3Parser().readContents(yamlContent, null, null).getOpenAPI();
        }
    }
}

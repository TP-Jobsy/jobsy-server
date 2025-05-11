package com.example.jobsyserver.features.common.validation.client.impl;

import com.example.jobsyserver.features.common.validation.client.RestCountriesClient;
import com.example.jobsyserver.features.common.validation.dto.RestCountryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestCountriesClientImpl implements RestCountriesClient {
    private final WebClient restCountriesClient;

    @Override
    public List<RestCountryDto> fetchAllCountries() {
        return restCountriesClient.get()
                .uri(uri -> uri
                        .path("/all")
                        .queryParam("fields", "translations,cca2,name")
                        .build())
                .retrieve()
                .bodyToFlux(RestCountryDto.class)
                .collectList()
                .block();
    }
}

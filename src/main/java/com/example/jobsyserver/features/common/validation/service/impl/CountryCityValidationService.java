package com.example.jobsyserver.features.common.validation.service.impl;

import com.example.jobsyserver.features.common.validation.dto.CountriesNowResponse;
import com.example.jobsyserver.features.common.validation.service.LocationValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CountryCityValidationService implements LocationValidationService {

    private final WebClient restCountries;
    private final WebClient countriesNow;

    public CountryCityValidationService(
            @Qualifier("restCountriesClient") WebClient restCountries,
            @Qualifier("countriesNowClient")  WebClient countriesNow
    ) {
        this.restCountries = restCountries;
        this.countriesNow  = countriesNow;
    }

    @Override
    public boolean countryExists(String country) {
        try {
            restCountries.get()
                    .uri(uri -> uri.path("/name/{name}")
                            .queryParam("fullText", "true")
                            .build(country))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound ex) {
            return false;
        }
    }

    @Override
    public boolean cityExistsInCountry(String country, String city) {
        return fetchCities(country).stream()
                .anyMatch(name -> name.equalsIgnoreCase(city));
    }

    private List<String> fetchCities(String country) {
        try {
            CountriesNowResponse resp = countriesNow.post()
                    .uri("/countries/cities")
                    .bodyValue(Map.of("country", country))
                    .retrieve()
                    .bodyToMono(CountriesNowResponse.class)
                    .block();
            if (resp != null && !resp.error()) {
                return resp.data();
            }
        } catch (Exception ignore) { }
        return Collections.emptyList();
    }
}
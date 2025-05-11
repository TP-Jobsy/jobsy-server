package com.example.jobsyserver.features.common.validation.client.impl;

import com.example.jobsyserver.features.common.config.geonames.GeoNamesProperties;
import com.example.jobsyserver.features.common.validation.client.GeoNamesClient;
import com.example.jobsyserver.features.common.validation.dto.CitySearchResponse;
import com.example.jobsyserver.features.common.validation.dto.CountryInfoResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeoNamesClientImpl implements GeoNamesClient {
    private final WebClient webClient;
    private final GeoNamesProperties props;

    public GeoNamesClientImpl(
            @Qualifier("geoNamesClient") WebClient webClient,
            GeoNamesProperties props
    ) {
        this.webClient = webClient;
        this.props     = props;
    }

    @Override
    public CountryInfoResponse fetchAllCountries(String lang) {
        return webClient.get()
                .uri(uri -> uri
                        .path("/countryInfoJSON")
                        .queryParam("username", props.username())
                        .queryParam("lang", lang)
                        .build())
                .retrieve()
                .bodyToMono(CountryInfoResponse.class)
                .block();
    }

    @Override
    public CitySearchResponse searchCity(String nameEquals, String countryCode, int maxRows) {
        return webClient.get()
                .uri(uri -> uri
                        .path("/searchJSON")
                        .queryParam("username", props.username())
                        .queryParam("name_equals", nameEquals)
                        .queryParam("country", countryCode)
                        .queryParam("maxRows", maxRows)
                        .build())
                .retrieve()
                .bodyToMono(CitySearchResponse.class)
                .block();
    }
}

package com.example.jobsyserver;

import com.example.jobsyserver.features.common.validation.service.CityLookupService;
import com.example.jobsyserver.features.common.validation.service.CountryLookupService;
import com.example.jobsyserver.features.common.validation.service.LocationValidationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@TestConfiguration
public class TestValidationStubs {

    @Bean
    public WebClient geoNamesClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebClient countriesNowClient() {
        return WebClient.builder().build();
    }

    @Bean
    public CountryLookupService countryLookupService() {
        return new CountryLookupService() {
            @Override
            public Optional<String> findIso2ByName(String name) {
                return Optional.of("RU");
            }

            @Override
            public boolean existsByName(String name) {
                return true;
            }
        };
    }

    @Bean
    public CityLookupService cityLookupService() {
        return (city, code) -> true;
    }

    @Bean
    public LocationValidationService locationValidationService(
            CountryLookupService countryLookup,
            CityLookupService cityLookup
    ) {
        return new LocationValidationService() {
            @Override
            public boolean countryExists(String country) { return true; }
            @Override
            public boolean cityExistsInCountry(String country, String city) { return true; }
        };
    }
}
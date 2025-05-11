package com.example.jobsyserver.features.common.validation.service.impl;

import com.example.jobsyserver.features.common.validation.client.GeoNamesClient;
import com.example.jobsyserver.features.common.validation.dto.CountryInfo;
import com.example.jobsyserver.features.common.validation.service.CountryLookupService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoNamesCountryLookupService implements CountryLookupService {
    private final GeoNamesClient client;

    private List<CountryInfo> cache;

    @PostConstruct
    private void init() {
        cache = client.fetchAllCountries("ru").geonames();
    }

    @Override
    public Optional<String> findIso2ByName(String name) {
        return cache.stream()
                .filter(c -> c.countryName().equalsIgnoreCase(name.trim()))
                .map(CountryInfo::countryCode)
                .findFirst();
    }

    @Override
    public boolean existsByName(String name) {
        return findIso2ByName(name).isPresent();
    }
}

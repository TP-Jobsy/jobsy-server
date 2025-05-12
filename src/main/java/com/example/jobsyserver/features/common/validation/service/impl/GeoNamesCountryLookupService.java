package com.example.jobsyserver.features.common.validation.service.impl;

import com.example.jobsyserver.features.common.validation.client.GeoNamesClient;
import com.example.jobsyserver.features.common.validation.dto.CountryInfo;
import com.example.jobsyserver.features.common.validation.service.CountryLookupService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("!test")
@RequiredArgsConstructor
public class GeoNamesCountryLookupService implements CountryLookupService {
    private final GeoNamesClient client;

    private List<CountryInfo> cache;

    @PostConstruct
    private void init() {
        try {
            var resp = client.fetchAllCountries("ru");
            if (resp != null && resp.geonames() != null) {
                cache = resp.geonames();
            }
        } catch (Exception ex) {
            System.err.println("Не удалось загрузить список стран из GeoNames: " + ex.getMessage());
            cache = List.of();
        }
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

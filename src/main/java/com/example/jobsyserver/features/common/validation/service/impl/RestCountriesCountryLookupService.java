package com.example.jobsyserver.features.common.validation.service.impl;

import com.example.jobsyserver.features.common.validation.client.RestCountriesClient;
import com.example.jobsyserver.features.common.validation.dto.RestCountryDto;
import com.example.jobsyserver.features.common.validation.service.CountryLookupService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
@RequiredArgsConstructor
public class RestCountriesCountryLookupService implements CountryLookupService {

    private final RestCountriesClient client;
    private final Map<String, String> nameToCca2 = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<RestCountryDto> all = client.fetchAllCountries();
        if (all == null) return;
        for (RestCountryDto c : all) {
            String code = c.cca2();
            nameToCca2.put(c.name().common().toLowerCase(Locale.ROOT), code);
            var rus = c.translations().get("rus");
            if (rus != null && rus.common() != null) {
                nameToCca2.put(rus.common().toLowerCase(Locale.ROOT), code);
            }
        }
    }

    @Override
    public Optional<String> findIso2ByName(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(nameToCca2.get(name.trim().toLowerCase(Locale.ROOT)));
    }

    @Override
    public boolean existsByName(String name) {
        return findIso2ByName(name).isPresent();
    }
}

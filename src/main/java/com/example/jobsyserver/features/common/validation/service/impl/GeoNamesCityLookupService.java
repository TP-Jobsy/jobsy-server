package com.example.jobsyserver.features.common.validation.service.impl;

import com.example.jobsyserver.features.common.validation.client.GeoNamesClient;
import com.example.jobsyserver.features.common.validation.service.CityLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoNamesCityLookupService implements CityLookupService {
    private final GeoNamesClient client;

    @Override
    public boolean existsInCountry(String city, String countryCode) {
        var resp = client.searchCity(city.trim(), countryCode, 1);
        return resp.geonames() != null && !resp.geonames().isEmpty();
    }
}

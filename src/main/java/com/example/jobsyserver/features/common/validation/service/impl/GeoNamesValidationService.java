package com.example.jobsyserver.features.common.validation.service.impl;

import com.example.jobsyserver.features.common.validation.service.CityLookupService;
import com.example.jobsyserver.features.common.validation.service.CountryLookupService;
import com.example.jobsyserver.features.common.validation.service.LocationValidationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Service
@Profile("!test")
public class GeoNamesValidationService implements LocationValidationService {

    private final CountryLookupService countryLookup;
    private final CityLookupService cityLookup;

    public GeoNamesValidationService(CountryLookupService countryLookup,
                                     CityLookupService cityLookup) {
        this.countryLookup = countryLookup;
        this.cityLookup    = cityLookup;
    }

    @Override
    public boolean countryExists(String country) {
        return countryLookup.existsByName(country);
    }

    @Override
    public boolean cityExistsInCountry(String country, String city) {
        return countryLookup.findIso2ByName(country)
                .map(code -> cityLookup.existsInCountry(city, code))
                .orElse(false);
    }
}
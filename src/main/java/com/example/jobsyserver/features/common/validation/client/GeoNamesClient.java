package com.example.jobsyserver.features.common.validation.client;

import com.example.jobsyserver.features.common.validation.dto.CitySearchResponse;
import com.example.jobsyserver.features.common.validation.dto.CountryInfoResponse;

public interface GeoNamesClient {
    CountryInfoResponse fetchAllCountries(String lang);
    CitySearchResponse searchCity(String nameEquals, String countryCode, int maxRows);
}

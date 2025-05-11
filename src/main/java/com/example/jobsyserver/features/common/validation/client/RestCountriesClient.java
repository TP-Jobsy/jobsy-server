package com.example.jobsyserver.features.common.validation.client;

import com.example.jobsyserver.features.common.validation.dto.RestCountryDto;

import java.util.List;

public interface RestCountriesClient {
    List<RestCountryDto> fetchAllCountries();
}

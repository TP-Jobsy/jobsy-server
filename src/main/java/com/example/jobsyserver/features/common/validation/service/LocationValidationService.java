package com.example.jobsyserver.features.common.validation.service;

public interface LocationValidationService {
    boolean countryExists(String country);
    boolean cityExistsInCountry(String country, String city);
}

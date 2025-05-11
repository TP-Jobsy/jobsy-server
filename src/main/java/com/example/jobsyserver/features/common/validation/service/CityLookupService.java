package com.example.jobsyserver.features.common.validation.service;

public interface CityLookupService {
    boolean existsInCountry(String city, String countryCode);
}

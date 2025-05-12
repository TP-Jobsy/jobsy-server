package com.example.jobsyserver.features.common.validation.service;

import java.util.Optional;

public interface CountryLookupService {
    Optional<String> findIso2ByName(String name);
    boolean existsByName(String name);
}

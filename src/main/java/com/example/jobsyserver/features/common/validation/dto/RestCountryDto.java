package com.example.jobsyserver.features.common.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RestCountryDto(
        Name name,
        String cca2,
        Map<String, Translation> translations
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Name(String common, String official) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Translation(String common, String official) {}
}

package com.example.jobsyserver.features.common.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CityInfo(String name) {}

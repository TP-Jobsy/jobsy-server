package com.example.jobsyserver.features.common.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CitySearchResponse(List<CityInfo> geonames) {}

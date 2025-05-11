package com.example.jobsyserver.features.common.validation.dto;

import java.util.List;

public record CountriesNowResponse (
        boolean error,
        String msg,
        List<String> data
) {}

package com.example.jobsyserver.features.common.validation.validator;

import com.example.jobsyserver.features.common.validation.service.LocationValidationService;
import com.example.jobsyserver.features.common.validation.annotation.ValidCountry;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryValidator implements ConstraintValidator<ValidCountry, String> {

    private final LocationValidationService locationService;

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        if (country == null || country.isBlank()) {
            return true;
        }
        return locationService.countryExists(country);
    }
}
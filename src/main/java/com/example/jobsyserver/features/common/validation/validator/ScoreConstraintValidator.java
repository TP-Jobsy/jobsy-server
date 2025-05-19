package com.example.jobsyserver.features.common.validation.validator;

import com.example.jobsyserver.features.common.validation.annotation.ValidScore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ScoreConstraintValidator implements ConstraintValidator<ValidScore, Integer> {

    @Override
    public void initialize(ValidScore constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= 1 && value <= 5;
    }
}
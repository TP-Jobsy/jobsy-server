package com.example.jobsyserver.features.common.validation.annotation;

import com.example.jobsyserver.features.common.validation.validator.CountryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = CountryValidator.class)
@Documented
public @interface ValidCountry {
    String message() default "Страна не найдена";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
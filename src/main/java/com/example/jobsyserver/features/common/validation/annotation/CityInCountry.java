package com.example.jobsyserver.features.common.validation.annotation;

import com.example.jobsyserver.features.common.validation.validator.CityInCountryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = CityInCountryValidator.class)
@Documented
public @interface CityInCountry {
    String message() default "Город не найден в указанной стране";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String countryField() default "country";
    String cityField()    default "city";
}

package com.example.jobsyserver.features.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AdultValidator.class)
public @interface Adult {
    String message() default "Пользователь должен быть старше {value} лет";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int value() default 18;
}

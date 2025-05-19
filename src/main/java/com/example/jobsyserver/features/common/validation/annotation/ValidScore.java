package com.example.jobsyserver.features.common.validation.annotation;

import com.example.jobsyserver.features.common.validation.validator.ScoreConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ScoreConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidScore {
    String message() default "Оценка должна быть от 1 до 5";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

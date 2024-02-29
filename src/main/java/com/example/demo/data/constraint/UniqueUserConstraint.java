package com.example.demo.data.constraint;

import com.example.demo.data.validator.UniqueUserConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(PARAMETER)
@Constraint(validatedBy = UniqueUserConstraintValidator.class)
public @interface UniqueUserConstraint {
    String message() default "The user already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
package com.example.projecttracker.util.annotation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TaskDateValidator.class})
public @interface TaskDate {
    String message() default "Invalid Date! Date cant be before the project start date!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}

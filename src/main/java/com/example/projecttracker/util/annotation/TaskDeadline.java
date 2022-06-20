package com.example.projecttracker.util.annotation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This is an annotation that validates the deadline of a task.
 *
 * @author Alyssa Heimlicher
 * @version 1.2
 * @since 2022-06-20
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {TaskDeadlineValidator.class})
public @interface TaskDeadline {
    /**
     * The message to display if the deadline is invalid.
     * @return The message to display if the deadline is invalid.
     * @author Alyssa Heimlicher
     */
    String message() default "Invalid deadline! Deadline cant be before the project start date!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}

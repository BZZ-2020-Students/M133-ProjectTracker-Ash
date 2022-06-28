package com.example.projecttracker.util.annotation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This is an annotation that validates the severity of an issue.
 *
 * @author Alyssa Heimlicher
 * @version 1.2
 * @since 2022-06-20
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IssueSeverityValidator.class})
public @interface IssueSeverity {
    /**
     * The message to display.
     *
     * @return The message to display.
     * @since 1.2
     */
    String message() default "Invalid Severity! Severity must be one of the following: Critical, Major, Minor, Trivial";

    /**
     * The groups the constraint belongs to.
     *
     * @return The groups the constraint belongs to.
     * @since 1.2
     */
    Class<?>[] groups() default {};

    /**
     * The payload of the constraint.
     *
     * @return The payload of the constraint.
     * @since 1.2
     */
    Class<?>[] payload() default {};
}

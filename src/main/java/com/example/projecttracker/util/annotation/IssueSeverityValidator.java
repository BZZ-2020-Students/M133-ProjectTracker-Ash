package com.example.projecttracker.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;

import static com.example.projecttracker.util.Constants.VALID_SEVERITIES;

/**
 * This is a ConstraintValidator class that validates the severity of an issue.
 *
 * @author Alyssa Heimlicher
 * @version 1.2
 * @since 2022-06-20
 */
public class IssueSeverityValidator implements ConstraintValidator<IssueSeverity, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(VALID_SEVERITIES).contains(s.toLowerCase(Locale.ROOT));
    }
}

package com.example.projecttracker.util.annotation;

import jakarta.validation.ConstraintValidator;
import static com.example.projecttracker.util.Constants.*;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class IssueSeverityValidator implements ConstraintValidator<IssueSeverity, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(VALID_SEVERITIES).contains(s);
    }
}

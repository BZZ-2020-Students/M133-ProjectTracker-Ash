package com.example.projecttracker.util.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * This is a ConstraintValidator class that validates the deadline of a task.
 *
 * @author Alyssa Heimlicher
 * @version 1.2
 * @since 2022-06-20
 */
public class TaskDeadlineValidator implements ConstraintValidator<TaskDeadline, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        return true;
    }
}

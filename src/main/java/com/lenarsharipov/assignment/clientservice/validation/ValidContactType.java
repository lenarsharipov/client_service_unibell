package com.lenarsharipov.assignment.clientservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContactTypeValidator.class)
public @interface ValidContactType {
    String message() default "Invalid contact type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


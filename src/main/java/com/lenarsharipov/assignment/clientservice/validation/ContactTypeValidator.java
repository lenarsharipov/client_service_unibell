package com.lenarsharipov.assignment.clientservice.validation;

import com.lenarsharipov.assignment.clientservice.model.ContactType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ContactTypeValidator implements ConstraintValidator<ValidContactType, String> {

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        if (type == null) {
            return true;
        }

        return Arrays.stream(ContactType.values())
                .anyMatch(contactType -> contactType.name().equalsIgnoreCase(type));
    }
}

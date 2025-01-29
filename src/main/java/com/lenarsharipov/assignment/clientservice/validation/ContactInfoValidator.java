package com.lenarsharipov.assignment.clientservice.validation;

import com.lenarsharipov.assignment.clientservice.dto.AddContactDto;
import com.lenarsharipov.assignment.clientservice.model.ContactType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ContactInfoValidator implements ConstraintValidator<ValidContactInfo, AddContactDto> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    @Override
    public boolean isValid(AddContactDto addContactDto, ConstraintValidatorContext context) {
        if (addContactDto == null) {
            return true;
        }

        ContactType type = addContactDto.type();
        String value = addContactDto.value();

        if (type == ContactType.EMAIL) {
            if (!EMAIL_PATTERN.matcher(value).matches()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid email format")
                        .addPropertyNode("value")
                        .addConstraintViolation();
                return false;
            }
        } else if (type == ContactType.PHONE) {
            if (!PHONE_PATTERN.matcher(value).matches()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid phone format")
                        .addPropertyNode("value")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}

package com.lenarsharipov.assignment.clientservice.dto;

import com.lenarsharipov.assignment.clientservice.model.ContactType;

public record ContactReadDto(
        ContactType type,
        String value
) {
}

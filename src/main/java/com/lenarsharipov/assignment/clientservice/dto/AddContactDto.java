package com.lenarsharipov.assignment.clientservice.dto;

import com.lenarsharipov.assignment.clientservice.model.ContactType;
import com.lenarsharipov.assignment.clientservice.validation.ValidContactInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Contact info")
@ValidContactInfo
public record AddContactDto(
        @Schema(description = "Contact tye", example = "PHONE")
        @NotNull ContactType type,

        @Schema(description = "value of contact info", example = "123456789 or test@test.com")
        @NotNull String value
) {
}

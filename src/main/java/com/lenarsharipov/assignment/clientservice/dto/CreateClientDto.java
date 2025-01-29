package com.lenarsharipov.assignment.clientservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "New client")
public record CreateClientDto(

        @Schema(description = "client name", example = "Peter Jackson")
        @NotNull
        @Length(min = 2, max = 50)
        String clientName
) {
}

package com.lenarsharipov.assignment.clientservice.dto;

import lombok.Builder;

@Builder
public record ClientReadDto(
        Long id,
        String name
) {
}

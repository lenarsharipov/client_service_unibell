package com.lenarsharipov.assignment.clientservice.mapper;

import com.lenarsharipov.assignment.clientservice.dto.ClientReadDto;
import com.lenarsharipov.assignment.clientservice.dto.CreateClientDto;
import com.lenarsharipov.assignment.clientservice.model.Client;

public class ClientMapper {

    public static ClientReadDto toClientReadDto(Client client) {
        return ClientReadDto.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }

    public static Client toClient(CreateClientDto request) {
        return Client.builder()
                .name(request.clientName())
                .build();
    }
}

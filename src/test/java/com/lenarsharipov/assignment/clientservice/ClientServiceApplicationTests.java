package com.lenarsharipov.assignment.clientservice;

import com.lenarsharipov.assignment.clientservice.dto.ClientReadDto;
import com.lenarsharipov.assignment.clientservice.dto.CreateClientDto;
import com.lenarsharipov.assignment.clientservice.exception.ResourceNotFoundException;
import com.lenarsharipov.assignment.clientservice.repository.PessimisticLockClientRepository;
import com.lenarsharipov.assignment.clientservice.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ClientServiceApplicationTests {

    @Autowired
    PessimisticLockClientRepository clientRepository;

    @Autowired
    ClientService clientService;

    @BeforeEach
    void setup() {
        clientRepository.beginTransaction();
        clientRepository.findAll()
                .forEach(c -> clientRepository.deleteById(c.getId()));
        clientRepository.commitTransaction();
    }

    @Test
    void shouldSaveClientAndReturnDto() {
        CreateClientDto dto = new CreateClientDto("testName");
        ClientReadDto clientReadDto = clientService.saveClient(dto);

        assertThat(clientReadDto).isNotNull();
        assertThat(clientRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldReturnClientDtoFoundById() {
        CreateClientDto dto = new CreateClientDto("testName");
        ClientReadDto clientReadDto = clientService.saveClient(dto);

        assertThat(clientService.findClientById(clientReadDto.id())).isNotNull();
        assertThat(clientService.findClientById(clientReadDto.id())).isEqualTo(clientReadDto);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenClientNotFound() {
        assertThatThrownBy(() -> clientService.findClientById(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldDeleteClientById() {
        CreateClientDto dto = new CreateClientDto("testName");
        ClientReadDto clientReadDto = clientService.saveClient(dto);

        clientService.deleteClientById(clientReadDto.id());

        assertThat(clientService.findAllClients()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenPassedInvalidId() {
        assertThatThrownBy(() -> clientService.deleteClientById(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}

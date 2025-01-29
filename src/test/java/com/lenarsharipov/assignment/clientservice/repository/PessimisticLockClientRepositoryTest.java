package com.lenarsharipov.assignment.clientservice.repository;

import com.lenarsharipov.assignment.clientservice.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class PessimisticLockClientRepositoryTest {

    PessimisticLockClientRepository clientRepository;

    @BeforeEach
    void setup() {
        clientRepository = new PessimisticLockClientRepository();
    }

    @Test
    void shouldCreateNewClient() {
        Client client = buildTestClient();
        clientRepository.beginTransaction();
        clientRepository.save(client);
        clientRepository.commitTransaction();

        assertAll(
                () -> assertThat(client).isNotNull(),
                () -> assertThat(client.getId()).isNotNull(),
                () -> assertThat(clientRepository.findAll().getFirst()).isEqualTo(client)
        );
    }

    private static Client buildTestClient() {
        return Client.builder()
                .name("testName")
                .build();
    }

    @Test
    void shouldReturnEmptyClientList() {
        assertThat(clientRepository.findAll()).hasSize(0);
    }

    @Test
    void shouldReturnNonEmptyClientsList() {
        Client client = buildTestClient();
        clientRepository.beginTransaction();
        clientRepository.save(client);
        clientRepository.commitTransaction();

        assertAll(
                () -> assertThat(clientRepository.findAll()).hasSize(1),
                () -> assertThat(clientRepository.findAll()).isEqualTo(List.of(client))
        );
    }

    @Test
    void shouldReturnClientByValidId() {
        Client client = buildTestClient();
        clientRepository.beginTransaction();
        clientRepository.save(client);
        clientRepository.commitTransaction();

        assertThat(clientRepository.findById(client.getId())).isPresent();
        assertThat(clientRepository.findById(client.getId()).get()).isEqualTo(client);
    }

    @Test
    void shouldReturnEmptyOptionalWhenClientNotFound() {
        assertThat(clientRepository.findById(0L)).isEmpty();
    }

    @Test
    void shouldRemoveClientById() {
        Client client = buildTestClient();
        clientRepository.beginTransaction();
        clientRepository.save(client);
        clientRepository.commitTransaction();

        clientRepository.deleteById(client.getId());

        assertThat(clientRepository.findAll()).isEmpty();
        assertThat(clientRepository.findById(client.getId())).isEmpty();
    }

    @Test
    void shouldNotSaveAfterRollback() {
        Client client = buildTestClient();
        clientRepository.beginTransaction();
        clientRepository.save(client);
        clientRepository.rollbackTransaction();

        assertThat(clientRepository.findAll()).isEmpty();
    }
}
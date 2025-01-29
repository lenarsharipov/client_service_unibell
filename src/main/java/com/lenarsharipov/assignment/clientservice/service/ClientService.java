package com.lenarsharipov.assignment.clientservice.service;

import com.lenarsharipov.assignment.clientservice.dto.AddContactDto;
import com.lenarsharipov.assignment.clientservice.dto.ClientReadDto;
import com.lenarsharipov.assignment.clientservice.dto.ContactReadDto;
import com.lenarsharipov.assignment.clientservice.dto.CreateClientDto;
import com.lenarsharipov.assignment.clientservice.exception.ResourceNotFoundException;
import com.lenarsharipov.assignment.clientservice.mapper.ClientMapper;
import com.lenarsharipov.assignment.clientservice.mapper.ContactMapper;
import com.lenarsharipov.assignment.clientservice.model.Client;
import com.lenarsharipov.assignment.clientservice.model.Contact;
import com.lenarsharipov.assignment.clientservice.model.ContactType;
import com.lenarsharipov.assignment.clientservice.repository.PessimisticLockClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final PessimisticLockClientRepository clientRepository;

    public ClientReadDto saveClient(CreateClientDto createClientDto) {
        log.debug("Save client {}", createClientDto);
        clientRepository.beginTransaction();
        try {
            Client client = ClientMapper.toClient(createClientDto);
            clientRepository.save(client);
            clientRepository.commitTransaction();
            log.debug("Saved client successfully {}", createClientDto);
            return ClientMapper.toClientReadDto(client);
        } catch (Exception e) {
            log.error("Save client failed {}", createClientDto);
            clientRepository.rollbackTransaction();
            throw e;
        }
    }

    public ClientReadDto findClientById(Long id) {
        log.debug("Find client by id {}", id);
        Client client = getClient(id);
        return ClientMapper.toClientReadDto(client);
    }

    private Client getClient(Long id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        return clientOpt.orElseThrow(() ->
                new ResourceNotFoundException("Client with ID " + id + " not found"));
    }

    public void deleteClientById(Long id) {
        log.debug("Delete client by id {}", id);
        clientRepository.beginTransaction();
        try {
            getClient(id);
            clientRepository.deleteById(id);
            clientRepository.commitTransaction();
            log.debug("Deleted client successfully {}", id);
        } catch (Exception e) {
            log.error("Delete client failed {}", id);
            clientRepository.rollbackTransaction();
            throw e;
        }
    }

    public void addContact(Long clientId, AddContactDto dto) {
        log.debug("Add contact {} to client with id {}", dto, clientId);
        clientRepository.beginTransaction();
        try {
            Client client = getClient(clientId);
            client.getContacts().add(ContactMapper.toContact(dto));
            clientRepository.save(client);
            clientRepository.commitTransaction();
            log.debug("Added contact successfully");
        } catch (Exception e) {
            log.error("Add contact failed {}", dto);
            clientRepository.rollbackTransaction();
            throw e;
        }
    }

    public List<ClientReadDto> findAllClients() {
        log.debug("Find all client list");
        return clientRepository.findAll().stream()
                .map(ClientMapper::toClientReadDto)
                .collect(toList());
    }

    public List<ContactReadDto> getClientContacts(Long clientId, String type) {
        log.debug("Find client contact list by type {}", type);
        if (type == null) {
            return ContactMapper.toContactDtoList(getClient(clientId).getContacts());
        }

        ContactType contactType = ContactType.valueOf(type.toUpperCase());
        List<Contact> contacts = getClient(clientId).getContacts().stream()
                .filter(c -> c.getType().equals(contactType))
                .collect(toList());
        return ContactMapper.toContactDtoList(contacts);
    }
}

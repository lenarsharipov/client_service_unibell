package com.lenarsharipov.assignment.clientservice.controller;

import com.lenarsharipov.assignment.clientservice.dto.AddContactDto;
import com.lenarsharipov.assignment.clientservice.dto.ClientReadDto;
import com.lenarsharipov.assignment.clientservice.dto.ContactReadDto;
import com.lenarsharipov.assignment.clientservice.dto.CreateClientDto;
import com.lenarsharipov.assignment.clientservice.service.ClientService;
import com.lenarsharipov.assignment.clientservice.validation.ValidContactType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Client Controller", description = "Project API")
public class ClientController {

    public static final String CLIENT_PATH = "/api/clients";
    public static final String CLIENT_ID_PATH = CLIENT_PATH + "/{clientId}";
    public static final String CLIENT_ID_CONTACT_PATH = CLIENT_ID_PATH + "/contacts";

    private final ClientService clientService;

    @Operation(summary = "Create new client with passed values")
    @PostMapping(CLIENT_PATH)
    @ResponseStatus(CREATED)
    public ClientReadDto addClient(@Valid @RequestBody CreateClientDto clientDto) {
        log.debug("Adding client: {}", clientDto);
        return clientService.saveClient(clientDto);
    }

    @Operation(summary = "Get list of client contacts. Result may be filtered by contact type")
    @GetMapping(CLIENT_ID_CONTACT_PATH)
    public List<ContactReadDto> getClientContactsByType(@PathVariable Long clientId,
                                                        @RequestParam(required = false)
                                                        @ValidContactType String type) {
        log.debug("Getting client contacts for clientId: {}", clientId);
        return clientService.getClientContacts(clientId, type);
    }

    @Operation(summary = "Add a contact info of EMAIL/PHONE type to existing client")
    @PostMapping(CLIENT_ID_CONTACT_PATH)
    public String addClientContact(@PathVariable Long clientId,
                                          @Valid @RequestBody AddContactDto dto) {
        log.debug("Adding client contact debug: {}", dto);
        clientService.addContact(clientId, dto);
        return dto.type() + " : " + dto.value() + " added successfully.";
    }

    @Operation(summary = "get list of all persisted clients")
    @GetMapping(CLIENT_PATH)
    public List<ClientReadDto> getAllClients() {
        log.debug("Getting all clients");
        return clientService.findAllClients();
    }

    @Operation(summary = "delete a client by id")
    @DeleteMapping(CLIENT_ID_PATH)
    @ResponseStatus(NO_CONTENT)
    public void deleteClient(@PathVariable Long clientId) {
        log.debug("Deleting client with id: {}", clientId);
        clientService.deleteClientById(clientId);
    }

    @Operation(summary = "Get info of specific client")
    @GetMapping(CLIENT_ID_PATH)
    public ClientReadDto getClientById(@PathVariable Long clientId) {
        log.debug("Getting client with id: {}", clientId);
        return clientService.findClientById(clientId);
    }
}

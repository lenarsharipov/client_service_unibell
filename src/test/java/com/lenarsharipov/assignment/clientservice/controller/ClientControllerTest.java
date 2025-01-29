package com.lenarsharipov.assignment.clientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lenarsharipov.assignment.clientservice.dto.AddContactDto;
import com.lenarsharipov.assignment.clientservice.dto.ClientReadDto;
import com.lenarsharipov.assignment.clientservice.dto.CreateClientDto;
import com.lenarsharipov.assignment.clientservice.dto.ContactReadDto;
import com.lenarsharipov.assignment.clientservice.exception.ResourceNotFoundException;
import com.lenarsharipov.assignment.clientservice.model.ContactType;
import com.lenarsharipov.assignment.clientservice.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.lenarsharipov.assignment.clientservice.controller.ClientController.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldAddClientSuccessfully() throws Exception {
        CreateClientDto createClientDto = new CreateClientDto("Peter Jackson");
        ClientReadDto clientReadDto = new ClientReadDto(1L, "Peter Jackson");

        when(clientService.saveClient(any(CreateClientDto.class))).thenReturn(clientReadDto);

        mockMvc.perform(post(CLIENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createClientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(clientReadDto.id()))
                .andExpect(jsonPath("$.name").value(clientReadDto.name()));
    }

    @Test
    void shouldGetAllClients() throws Exception {
        List<ClientReadDto> clients = List.of(new ClientReadDto(1L, "Peter Jackson"));

        when(clientService.findAllClients()).thenReturn(clients);

        mockMvc.perform(get(CLIENT_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(clients.size()))
                .andExpect(jsonPath("$[0].id").value(clients.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(clients.get(0).name()));
    }

    @Test
    void shouldGetClientById() throws Exception {
        ClientReadDto clientReadDto = new ClientReadDto(1L, "Peter Jackson");

        when(clientService.findClientById(1L)).thenReturn(clientReadDto);

        mockMvc.perform(get(CLIENT_ID_PATH, clientReadDto.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientReadDto.id()))
                .andExpect(jsonPath("$.name").value(clientReadDto.name()));
    }

    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {
        long clientId = 1L;
        when(clientService.findClientById(clientId)).thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(get(CLIENT_ID_PATH, clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteClient() throws Exception {
        long clientId = 1L;
        doNothing().when(clientService).deleteClientById(clientId);

        mockMvc.perform(delete(CLIENT_ID_PATH, clientId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAddClientContact() throws Exception {
        long clientId = 1L;
        AddContactDto addContactDto = new AddContactDto(ContactType.EMAIL, "test@example.com");

        mockMvc.perform(post(CLIENT_ID_CONTACT_PATH, clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addContactDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("EMAIL : test@example.com added successfully."));

        verify(clientService).addContact(eq(1L), any(AddContactDto.class));
    }

    @Test
    void shouldGetClientContactsByType() throws Exception {
        List<ContactReadDto> contacts = List.of(new ContactReadDto(ContactType.EMAIL, "test@example.com"));
        long clientId = 1L;
        when(clientService.getClientContacts(clientId, "EMAIL")).thenReturn(contacts);

        mockMvc.perform(get("/api/clients/1/contacts?type=EMAIL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(contacts.size()))
                .andExpect(jsonPath("$[0].type").value("EMAIL"))
                .andExpect(jsonPath("$[0].value").value("test@example.com"));
    }
}

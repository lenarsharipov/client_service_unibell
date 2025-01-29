package com.lenarsharipov.assignment.clientservice.mapper;

import com.lenarsharipov.assignment.clientservice.dto.AddContactDto;
import com.lenarsharipov.assignment.clientservice.dto.ContactReadDto;
import com.lenarsharipov.assignment.clientservice.model.Contact;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ContactMapper {

    public static Contact toContact(AddContactDto dto) {
        return Contact.builder()
                .type(dto.type())
                .value(dto.value())
                .build();
    }

    public static List<ContactReadDto> toContactDtoList(List<Contact> contacts) {
        return contacts.stream()
                .map(ContactMapper::toContactDto)
                .collect(toList());
    }

    private static ContactReadDto toContactDto(Contact contact) {
        return new ContactReadDto(contact.getType(), contact.getValue());
    }
}

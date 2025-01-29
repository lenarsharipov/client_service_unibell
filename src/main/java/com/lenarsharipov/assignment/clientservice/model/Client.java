package com.lenarsharipov.assignment.clientservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private Long id;
    private String name;

    @Builder.Default
    private List<Contact> contacts = new ArrayList<>();
}

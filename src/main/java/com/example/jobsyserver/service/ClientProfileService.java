package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.client.*;

import java.util.List;

public interface ClientProfileService {
    ClientProfileDto getProfile();
    ClientProfileDto updateBasic(ClientProfileBasicDto basicDto);
    ClientProfileDto updateContact(ClientProfileContactDto contactDto);
    ClientProfileDto updateField(ClientProfileFieldDto fieldDto);
    void deleteAccount();
    List<ClientProfileDto> getAllClients();
    ClientProfileDto getClientProfileById(Long id);
}
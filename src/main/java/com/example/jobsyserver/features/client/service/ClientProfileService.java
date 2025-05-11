package com.example.jobsyserver.features.client.service;

import com.example.jobsyserver.features.client.dto.ClientProfileBasicDto;
import com.example.jobsyserver.features.client.dto.ClientProfileContactDto;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.dto.ClientProfileFieldDto;

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
package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.client.dto.ClientProfileDto;

import java.util.List;

public interface ClientAdminService {
    List<ClientProfileDto> getAll();

    ClientProfileDto getById(Long userId);

    void deactivate(Long userId);

    void delete(Long userId);

    void activate(Long userId);
}

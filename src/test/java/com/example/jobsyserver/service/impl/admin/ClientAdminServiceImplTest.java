package com.example.jobsyserver.service.impl.admin;

import com.example.jobsyserver.features.admin.service.impl.ClientAdminServiceImpl;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientAdminServiceImplTest {

    @Mock private ClientProfileRepository repo;
    @Mock private ClientProfileMapper mapper;
    @InjectMocks private ClientAdminServiceImpl service;

    @Test
    void getByUserId_WhenExists() {
        ClientProfile p = new ClientProfile(); p.setId(2L);
        ClientProfileDto dto = new ClientProfileDto();
        when(repo.findByUserId(2L)).thenReturn(Optional.of(p));
        when(mapper.toDto(p)).thenReturn(dto);
        var out = service.getById(2L);
        assertThat(out).isEqualTo(dto);
    }

    @Test
    void getByUserId_WhenMissing_Throws() {
        when(repo.findByUserId(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(3L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
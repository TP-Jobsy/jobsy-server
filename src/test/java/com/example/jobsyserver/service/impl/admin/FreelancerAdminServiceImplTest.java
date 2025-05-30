package com.example.jobsyserver.service.impl.admin;

import com.example.jobsyserver.features.admin.service.impl.FreelancerAdminServiceImpl;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
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
class FreelancerAdminServiceImplTest {

    @Mock private FreelancerProfileRepository repo;
    @Mock private FreelancerProfileMapper mapper;
    @InjectMocks private FreelancerAdminServiceImpl service;

    @Test
    void getByUserId_WhenExists() {
        FreelancerProfile p = new FreelancerProfile(); p.setId(5L);
        FreelancerProfileDto dto = new FreelancerProfileDto();
        when(repo.findByUserId(5L)).thenReturn(Optional.of(p));
        when(mapper.toDto(p)).thenReturn(dto);
        var out = service.getById(5L);
        assertThat(out).isEqualTo(dto);
    }

    @Test
    void getByUserId_WhenMissing_Throws() {
        when(repo.findByUserId(6L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(6L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
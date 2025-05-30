package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;

import java.util.List;

public interface FreelancerAdminService {
    List<FreelancerProfileDto> getAll();

    FreelancerProfileDto getById(Long userId);

    void deactivate(Long userId);

    void delete(Long userId);

    void activate(Long userId);
}

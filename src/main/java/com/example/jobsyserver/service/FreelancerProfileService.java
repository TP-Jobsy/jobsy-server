package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileUpdateDto;

import java.util.List;

public interface FreelancerProfileService {
    FreelancerProfileDto getProfile();
    FreelancerProfileDto updateProfile(FreelancerProfileUpdateDto updateDto);
    void deleteAccount();
    List<FreelancerProfileDto> getAllFreelancers();
    FreelancerProfileDto getFreelancerProfileById(Long id);
}

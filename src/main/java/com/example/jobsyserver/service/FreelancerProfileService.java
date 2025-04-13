package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.freelancer.*;

import java.util.List;

public interface FreelancerProfileService {
    FreelancerProfileDto getProfile();
    FreelancerProfileDto updateBasic(FreelancerProfileBasicDto basicDto);
    FreelancerProfileDto updateContact(FreelancerProfileContactDto contactDto);
    FreelancerProfileDto updateAbout(FreelancerProfileAboutDto aboutDto);
    FreelancerProfileDto updateUser(FreelancerProfileUserDto userDto);
    void deleteAccount();
    List<FreelancerProfileDto> getAllFreelancers();
    FreelancerProfileDto getFreelancerProfileById(Long id);
}

package com.example.jobsyserver.features.freelancer.service;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileAboutDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileBasicDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileContactDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreelancerProfileService {
    FreelancerProfileDto getProfile();

    FreelancerProfileDto updateBasic(FreelancerProfileBasicDto basicDto);

    FreelancerProfileDto updateContact(FreelancerProfileContactDto contactDto);

    FreelancerProfileDto updateAbout(FreelancerProfileAboutDto aboutDto);

    void deleteAccount();

    List<FreelancerProfileDto> getAllFreelancers();

    Page<FreelancerListItem> listFreelancers(Pageable pageable);

    FreelancerProfileDto getFreelancerProfileById(Long id);

    FreelancerProfileDto addSkill(Long skillId);

    FreelancerProfileDto removeSkill(Long skillId);

    List<SkillDto> getSkills();
}

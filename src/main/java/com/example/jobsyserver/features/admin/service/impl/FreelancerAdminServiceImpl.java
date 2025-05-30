package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.FreelancerAdminService;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FreelancerAdminServiceImpl implements FreelancerAdminService {

    private final UserRepository userRepository;
    private final FreelancerProfileRepository profileRepo;
    private final FreelancerProfileMapper mapper;

    @Override
    public List<FreelancerProfileDto> getAll() {
        return userRepository.findByRole(UserRole.FREELANCER).stream()
                .map(profileRepo::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FreelancerProfileDto getById(Long userId) {
        FreelancerProfile p = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль фрилансера", userId));
        return mapper.toDto(p);
    }

    @Override
    public void deactivate(Long userId) {
        toggleActive(userId, false);
    }

    @Override
    public void activate(Long userId) {
        toggleActive(userId, true);
    }

    @Override
    public void delete(Long userId) {
        User u = userRepository.findByIdAndRole(userId, UserRole.FREELANCER)
                .orElseThrow(() -> new ResourceNotFoundException("Фрилансер", userId));
        userRepository.delete(u);
    }

    private void toggleActive(Long userId, boolean active) {
        User u = userRepository.findByIdAndRole(userId, UserRole.FREELANCER)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        u.setIsActive(active);
        userRepository.save(u);
    }
}
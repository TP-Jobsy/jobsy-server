package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.ClientAdminService;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
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
public class ClientAdminServiceImpl implements ClientAdminService {

    private final UserRepository userRepository;
    private final ClientProfileRepository profileRepo;
    private final ClientProfileMapper mapper;

    @Override
    public List<ClientProfileDto> getAll() {
        return userRepository.findByRole(UserRole.CLIENT).stream()
                .map(profileRepo::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientProfileDto getById(Long userId) {
        ClientProfile p = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль заказчика", userId));
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
        User u = userRepository.findByIdAndRole(userId, UserRole.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Заказчик", userId));
        userRepository.delete(u);
    }

    private void toggleActive(Long userId, boolean active) {
        User u = userRepository.findByIdAndRole(userId, UserRole.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        u.setIsActive(active);
        userRepository.save(u);
    }
}
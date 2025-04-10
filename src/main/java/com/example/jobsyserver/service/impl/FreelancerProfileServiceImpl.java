package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileUpdateDto;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.FreelancerProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;
    private final FreelancerProfileMapper freelancerProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public FreelancerProfileDto getProfile() {
        String currentEmail = getCurrentUserEmail();
        log.info("Попытка получить профиль фрилансера для пользователя с email: {}", currentEmail);
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден с email: {}", currentEmail);
                    return new UserNotFoundException("Пользователь не найден с email: " + currentEmail);
                });
        FreelancerProfile profile = freelancerProfileRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Профиль фрилансера не найден для пользователя с email: {}", currentEmail);
                    return new UserNotFoundException("Профиль фрилансера не найден для пользователя с email: " + currentEmail);
                });
        log.info("Профиль фрилансера успешно найден для пользователя: {}", currentEmail);
        return freelancerProfileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto updateProfile(FreelancerProfileUpdateDto updateDto) {
        String currentEmail = getCurrentUserEmail();
        log.info("Попытка обновления профиля фрилансера для пользователя с email: {}", currentEmail);

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден с email: {}", currentEmail);
                    return new UserNotFoundException("Пользователь не найден с email: " + currentEmail);
                });
        FreelancerProfile profile = freelancerProfileRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Профиль фрилансера не найден для пользователя с email: {}", currentEmail);
                    return new UserNotFoundException("Профиль фрилансера не найден для пользователя с email: " + currentEmail);
                });

        log.info("Обновление данных профиля: {}", updateDto);
        freelancerProfileMapper.updateEntityFromDto(updateDto, profile);
        FreelancerProfile updatedProfile = freelancerProfileRepository.save(profile);
        log.info("Профиль фрилансера обновлен в базе для пользователя: {}", currentEmail);

        if (updateDto.getFirstName() != null) {
            user.setFirstName(updateDto.getFirstName());
            log.info("Обновлено имя пользователя на: {}", updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            user.setLastName(updateDto.getLastName());
            log.info("Обновлена фамилия пользователя на: {}", updateDto.getLastName());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
            log.info("Обновлен номер телефона пользователя на: {}", updateDto.getPhone());
        }
        userRepository.save(user);
        log.info("Общие данные пользователя обновлены для email: {}", currentEmail);

        return freelancerProfileMapper.toDto(updatedProfile);
    }

    @Override
    @Transactional
    public void deleteAccount() {
        String currentEmail = getCurrentUserEmail();
        log.info("Попытка удаления аккаунта для пользователя с email: {}", currentEmail);
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден с email: {}", currentEmail);
                    return new UserNotFoundException("Пользователь не найден с email: " + currentEmail);
                });
        userRepository.delete(user);
        log.info("Аккаунт пользователя с email {} успешно удалён", currentEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FreelancerProfileDto> getAllFreelancers() {
        List<FreelancerProfile> profiles = freelancerProfileRepository.findAll();
        if (profiles.isEmpty()) {
            log.warn("Фрилансеры не найдены");
        }
        return freelancerProfileMapper.toDtoList(profiles);
    }

    @Override
    @Transactional(readOnly = true)
    public FreelancerProfileDto getFreelancerProfileById(Long id) {
        FreelancerProfile profile = freelancerProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Фрилансер не найден с id: " + id));
        return freelancerProfileMapper.toDto(profile);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            log.error("Пользователь не аутентифицирован");
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        log.debug("Текущий пользователь: {}", email);
        return email;
    }

}
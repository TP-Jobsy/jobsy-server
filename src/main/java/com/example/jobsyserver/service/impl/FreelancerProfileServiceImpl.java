package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.*;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.FreelancerProfileService;
import com.example.jobsyserver.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final SecurityService securityService;

    @Override
    @Transactional(readOnly = true)
    public FreelancerProfileDto getProfile() {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        log.info("Получение профиля фрилансера для пользователя с email: {}", securityService.getCurrentUserEmail());
        return freelancerProfileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto updateBasic(FreelancerProfileBasicDto basicDto) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        log.info("Обновление базовой информации профиля для пользователя с email: {}", securityService.getCurrentUserEmail());

        if (basicDto.getCountry() != null) {
            profile.setCountry(basicDto.getCountry());
            log.info("Обновлена страна: {}", basicDto.getCountry());
        }
        if (basicDto.getCity() != null) {
            profile.setCity(basicDto.getCity());
            log.info("Обновлен город: {}", basicDto.getCity());
        }
        return saveAndReturnDto(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto updateContact(FreelancerProfileContactDto contactDto) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        log.info("Обновление контактной информации профиля для пользователя с email: {}", securityService.getCurrentUserEmail());

        if (contactDto.getContactLink() != null) {
            profile.setContactLink(contactDto.getContactLink());
            log.info("Обновлена контактная ссылка: {}", contactDto.getContactLink());
        }
        return saveAndReturnDto(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto updateAbout(FreelancerProfileAboutDto aboutDto) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        log.info("Обновление информации 'о себе' для пользователя с email: {}", securityService.getCurrentUserEmail());

        if (aboutDto.getCategoryId() != null) {
            profile.setCategoryId(aboutDto.getCategoryId());
            log.info("Обновлен идентификатор категории: {}", aboutDto.getCategoryId());
        }
        if (aboutDto.getSpecializationId() != null) {
            profile.setSpecializationId(aboutDto.getSpecializationId());
            log.info("Обновлен идентификатор специализации: {}", aboutDto.getSpecializationId());
        }
        if (aboutDto.getExperienceLevel() != null) {
            profile.setExperienceLevel(aboutDto.getExperienceLevel());
            log.info("Обновлен уровень опыта: {}", aboutDto.getExperienceLevel());
        }
        if (aboutDto.getAboutMe() != null) {
            profile.setAboutMe(aboutDto.getAboutMe());
            log.info("Обновлено описание 'о себе'");
        }
        return saveAndReturnDto(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto updateUser(FreelancerProfileUserDto userDto) {
        User user = getCurrentUser();
        log.info("Обновление данных пользователя для профиля фрилансера с email: {}", securityService.getCurrentUserEmail());

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
            log.info("Обновлено имя пользователя на: {}", userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
            log.info("Обновлена фамилия пользователя на: {}", userDto.getLastName());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
            log.info("Обновлен номер телефона пользователя на: {}", userDto.getPhone());
        }
        userRepository.save(user);
        log.info("Данные пользователя обновлены для email: {}", securityService.getCurrentUserEmail());
        FreelancerProfile profile = freelancerProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль фрилансера не найден для пользователя с email: " + securityService.getCurrentUserEmail()));
        return freelancerProfileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public void deleteAccount() {
        String currentEmail = securityService.getCurrentUserEmail();
        log.info("Удаление аккаунта для пользователя с email: {}", currentEmail);
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + currentEmail));
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

    private User getCurrentUser() {
        String currentEmail = securityService.getCurrentUserEmail();
        return userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + currentEmail));
    }

    private FreelancerProfile getCurrentFreelancerProfile() {
        User user = getCurrentUser();
        return freelancerProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль фрилансера не найден для пользователя с email: " + user.getEmail()));
    }

    private FreelancerProfileDto saveAndReturnDto(FreelancerProfile profile) {
        FreelancerProfile updatedProfile = freelancerProfileRepository.save(profile);
        return freelancerProfileMapper.toDto(updatedProfile);
    }
}
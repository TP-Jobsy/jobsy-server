package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.*;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.*;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.SkillRepository;
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
    private final SkillRepository skillRepository;
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
        User user = getCurrentUser();
        log.info("Обновление базовой информации профиля для пользователя с email: {}", securityService.getCurrentUserEmail());

        if (basicDto.getCountry() != null) {
            profile.setCountry(basicDto.getCountry());
            log.info("Обновлена страна: {}", basicDto.getCountry());
        }
        if (basicDto.getCity() != null) {
            profile.setCity(basicDto.getCity());
            log.info("Обновлен город: {}", basicDto.getCity());
        }
        if (basicDto.getFirstName() != null) {
            user.setFirstName(basicDto.getFirstName());
            log.info("Обновлено имя пользователя на: {}", basicDto.getFirstName());
        }
        if (basicDto.getLastName() != null) {
            user.setLastName(basicDto.getLastName());
            log.info("Обновлена фамилия пользователя на: {}", basicDto.getLastName());
        }
        if (basicDto.getPhone() != null) {
            user.setPhone(basicDto.getPhone());
            log.info("Обновлен номер телефона пользователя на: {}", basicDto.getPhone());
        }
        userRepository.save(user);
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
    @Transactional
    public FreelancerProfileDto addSkill(Long skillId) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        boolean exists = profile.getFreelancerSkills().stream()
                .anyMatch(fs -> fs.getSkill().getId().equals(skillId));
        if (exists) {
            log.info("Навык с id {} уже присутствует в профиле", skillId);
            return saveAndReturnDto(profile);
        }
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new UserNotFoundException("Навык не найден с id: " + skillId));
        FreelancerSkill fs = FreelancerSkill.builder()
                .id(new FreelancerSkillId(profile.getId(), skill.getId()))
                .freelancerProfile(profile)
                .skill(skill)
                .build();
        profile.getFreelancerSkills().add(fs);
        log.info("Навык с id {} успешно добавлен", skillId);
        return saveAndReturnDto(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto removeSkill(Long skillId) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        boolean removed = profile.getFreelancerSkills().removeIf(fs -> fs.getSkill().getId().equals(skillId));
        if (!removed) {
            throw new UserNotFoundException("Навык с id " + skillId + " не найден в профиле");
        }
        log.info("Навык с id {} успешно удалён", skillId);
        return saveAndReturnDto(profile);
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
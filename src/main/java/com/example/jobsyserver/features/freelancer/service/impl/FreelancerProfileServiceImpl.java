package com.example.jobsyserver.features.freelancer.service.impl;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileBasicDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileContactDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileAboutDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.category.service.CategoryService;
import com.example.jobsyserver.features.freelancer.service.FreelancerProfileService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import com.example.jobsyserver.features.specialization.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;
    private final FreelancerProfileMapper freelancerProfileMapper;
    private final SkillRepository skillRepository;
    private final SecurityService securityService;
    private final SkillMapper skillMapper;
    private final CategoryService categoryService;
    private final SpecializationService specializationService;

    @Override
    @Transactional(readOnly = true)
    public FreelancerProfileDto getProfile() {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        return mapWithNames(profile);
    }

    @Override
    @Transactional
    public FreelancerProfileDto updateBasic(FreelancerProfileBasicDto basicDto) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        User user = securityService.getCurrentUser();
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
        return mapWithNames(freelancerProfileRepository.save(profile));
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
        return mapWithNames(freelancerProfileRepository.save(profile));
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
        return mapWithNames(freelancerProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public void deleteAccount() {
        String currentEmail = securityService.getCurrentUserEmail();
        log.info("Удаление аккаунта для пользователя с email: {}", currentEmail);
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email", currentEmail));
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
        return profiles.stream()
                .map(this::mapWithNames)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public Page<FreelancerListItem> listFreelancers(Pageable pageable) {
        return freelancerProfileRepository.findAllProjected(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getSkills() {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        return profile.getSkills().stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FreelancerProfileDto addSkill(Long skillId) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        boolean exists = profile.getSkills().stream()
                .anyMatch(s -> s.getId().equals(skillId));
        if (exists) {
            log.info("Навык {} уже присутствует в профиле {}", skillId, profile.getId());
            return mapWithNames(profile);
        }
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Навык", skillId));
        profile.getSkills().add(skill);
        FreelancerProfile saved = freelancerProfileRepository.save(profile);
        log.info("Навык {} добавлен в профиль {}", skillId, profile.getId());
        return mapWithNames(saved);
    }

    @Override
    @Transactional
    public FreelancerProfileDto removeSkill(Long skillId) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        boolean removed = profile.getSkills().removeIf(s -> s.getId().equals(skillId));
        if (!removed) {
            log.warn("Навык {} не найден в профиле {}", skillId, profile.getId());
            throw new ResourceNotFoundException("Навык " + skillId + " не найден");
        }
        FreelancerProfile saved = freelancerProfileRepository.save(profile);
        log.info("Навык {} удалён из профиля {}", skillId, profile.getId());
        return mapWithNames(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FreelancerProfileDto getFreelancerProfileById(Long id) {
        FreelancerProfile profile = freelancerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фрилансер" + id));
        return mapWithNames(profile);
    }

    private FreelancerProfileDto mapWithNames(FreelancerProfile profile) {
        FreelancerProfileDto dto = freelancerProfileMapper.toDto(profile);
        if (dto.getAbout() == null) {
            dto.setAbout(new FreelancerProfileAboutDto());
        }
        Long catId = profile.getCategoryId();
        if (catId != null) {
            dto.getAbout().setCategoryName(categoryService.getCategoryById(catId).getName());
        }
        Long specId = profile.getSpecializationId();
        if (specId != null) {
            dto.getAbout().setSpecializationName(specializationService.getSpecializationById(specId).getName());
        }
        return dto;
    }

    private FreelancerProfile getCurrentFreelancerProfile() {
        User user = securityService.getCurrentUser();
        return freelancerProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль фрилансера", "email", user.getEmail()));
    }
}
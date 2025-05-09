package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileBasicDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileContactDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileAboutDto;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.FreelancerSkill;
import com.example.jobsyserver.model.FreelancerSkillId;
import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.SkillRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.CategoryService;
import com.example.jobsyserver.service.FreelancerProfileService;
import com.example.jobsyserver.service.SecurityService;
import com.example.jobsyserver.service.SpecializationService;
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
    private final CategoryService categoryService;
    private final SpecializationService specializationService;

    @Override
    @Transactional(readOnly = true)
    public FreelancerProfileDto getProfile() {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        FreelancerProfileDto dto = mapWithNames(profile);
        log.info("Получение профиля фрилансера для пользователя с email: {}", securityService.getCurrentUserEmail());
        return dto;
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
    @Transactional
    public FreelancerProfileDto addSkill(Long skillId) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        int MAX_SKILLS = 5;
        int current = profile.getFreelancerSkills().size();
        if (current >= MAX_SKILLS) {
            log.warn("Попытка добавить {}-й навык, но лимит {} достигнут для профиля id={}",
                    current + 1, MAX_SKILLS, profile.getId());
            throw new BadRequestException("Нельзя добавить более " + MAX_SKILLS + " навыков");
        }
        boolean exists = profile.getFreelancerSkills().stream()
                .anyMatch(fs -> fs.getSkill().getId().equals(skillId));
        if (exists) {
            log.info("Навык с id={} уже есть в профиле id={}", skillId, profile.getId());
            return mapWithNames(profile);
        }
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Навык" + skillId));
        FreelancerSkill fs = FreelancerSkill.builder()
                .id(new FreelancerSkillId(profile.getId(), skill.getId()))
                .freelancerProfile(profile)
                .skill(skill)
                .build();
        profile.getFreelancerSkills().add(fs);
        log.info("Навык с id={} добавлен в профиль id={}", skillId, profile.getId());
        return mapWithNames(freelancerProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public FreelancerProfileDto removeSkill(Long skillId) {
        FreelancerProfile profile = getCurrentFreelancerProfile();
        boolean removed = profile.getFreelancerSkills().removeIf(fs -> fs.getSkill().getId().equals(skillId));
        if (!removed) {
            throw new ResourceNotFoundException("Навык" + skillId);
        }
        log.info("Навык с id {} успешно удалён", skillId);
        return mapWithNames(freelancerProfileRepository.save(profile));
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
package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.client.ClientProfileBasicDto;
import com.example.jobsyserver.dto.client.ClientProfileContactDto;
import com.example.jobsyserver.dto.client.ClientProfileDto;
import com.example.jobsyserver.dto.client.ClientProfileFieldDto;
import com.example.jobsyserver.dto.client.ClientProfileUserDto;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.ClientProfileMapper;
import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ClientProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ClientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;
    private final ClientProfileMapper clientProfileMapper;
    private final SecurityServiceImpl securityService;

    @Override
    @Transactional(readOnly = true)
    public ClientProfileDto getProfile() { // todo: вынести в вспомогательные методы
        String email = securityService.getCurrentUserEmail();
        log.info("Получение профиля заказчика для пользователя с email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + email));
        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль заказчика не найден для пользователя с email: " + email));
        return clientProfileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public ClientProfileDto updateBasic(ClientProfileBasicDto basicDto) {
        String email = securityService.getCurrentUserEmail();
        log.info("Обновление базовых данных профиля для пользователя с email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + email));
        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль заказчика не найден для пользователя с email: " + email));

        if (basicDto.getCompanyName() != null) {
            profile.setCompanyName(basicDto.getCompanyName());
            log.info("Обновлено название компании: {}", basicDto.getCompanyName());
        }
        if (basicDto.getPosition() != null) {
            profile.setPosition(basicDto.getPosition());
            log.info("Обновлена должность: {}", basicDto.getPosition());
        }
        if (basicDto.getCountry() != null) {
            profile.setCountry(basicDto.getCountry());
            log.info("Обновлена страна: {}", basicDto.getCountry());
        }
        if (basicDto.getCity() != null) {
            profile.setCity(basicDto.getCity());
            log.info("Обновлен город: {}", basicDto.getCity());
        }
        ClientProfile updatedProfile = clientProfileRepository.save(profile);
        return clientProfileMapper.toDto(updatedProfile);
    }

    @Override
    @Transactional
    public ClientProfileDto updateContact(ClientProfileContactDto contactDto) {
        String email = securityService.getCurrentUserEmail();
        log.info("Обновление контактных данных профиля для пользователя с email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + email));
        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль заказчика не найден для пользователя с email: " + email));
        if (contactDto.getContactLink() != null) {
            profile.setContactLink(contactDto.getContactLink());
            log.info("Обновлена ссылка для связи: {}", contactDto.getContactLink());
        }
        ClientProfile updatedProfile = clientProfileRepository.save(profile);
        return clientProfileMapper.toDto(updatedProfile);
    }

    @Override
    @Transactional
    public ClientProfileDto updateField(ClientProfileFieldDto fieldDto) {
        String email = securityService.getCurrentUserEmail();
        log.info("Обновление информации о сфере деятельности профиля для пользователя с email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + email));
        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль заказчика не найден для пользователя с email: " + email));
        if (fieldDto.getFieldDescription() != null) {
            profile.setFieldDescription(fieldDto.getFieldDescription());
            log.info("Обновлено описание сферы деятельности: {}", fieldDto.getFieldDescription());
        }
        ClientProfile updatedProfile = clientProfileRepository.save(profile);
        return clientProfileMapper.toDto(updatedProfile);
    }

    @Override
    @Transactional
    public ClientProfileDto updateUser(ClientProfileUserDto userDto) {
        String email = securityService.getCurrentUserEmail();
        log.info("Обновление данных пользователя для профиля заказчика с email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + email));
        ClientProfile profile = clientProfileRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Профиль заказчика не найден для пользователя с email: " + email));

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
            log.info("Обновлён номер телефона пользователя на: {}", userDto.getPhone());
        }
        userRepository.save(user);
        log.info("Данные пользователя обновлены для email: {}", email);
        return clientProfileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public void deleteAccount() {
        String email = securityService.getCurrentUserEmail();
        log.info("Удаление аккаунта заказчика для пользователя с email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email: " + email));
        user.setIsActive(false);
        userRepository.save(user);
        log.info("Аккаунт заказчика с email {} успешно деактивирован", email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientProfileDto> getAllClients() {
        List<ClientProfile> profiles = clientProfileRepository.findAll();
        if (profiles.isEmpty()) {
            log.warn("Заказчики не найдены");
        }
        return clientProfileMapper.toDtoList(profiles);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientProfileDto getClientProfileById(Long id) {
        ClientProfile profile = clientProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Профиль заказчика не найден с id: " + id));
        return clientProfileMapper.toDto(profile);
    }
}
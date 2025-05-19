package com.example.jobsyserver.mapper;

import com.example.jobsyserver.TestValidationStubs;
import com.example.jobsyserver.features.client.dto.ClientProfileBasicDto;
import com.example.jobsyserver.features.client.dto.ClientProfileContactDto;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.dto.ClientProfileFieldDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapperImpl;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.user.mapper.UserMapperImpl;
import com.example.jobsyserver.features.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import({ UserMapperImpl.class, ClientProfileMapperImpl.class, TestValidationStubs.class })
@SpringBootTest
class ClientProfileMapperTest {

    @Autowired
    private ClientProfileMapper mapper;

    @Test
    void testToDto() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79991234567")
                .dateBirth(LocalDate.of(2000, 1, 1))
                .build();

        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 3, 30, 12, 0);

        ClientProfile profile = ClientProfile.builder()
                .id(1L)
                .user(user)
                .companyName("Acme Corp")
                .country("Россия")
                .city("Москва")
                .contactLink("http://acme-corp.example.com")
                .fieldDescription("Информационные технологии")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        ClientProfileDto dto = mapper.toDto(profile);

        assertNotNull(dto, "Полученный DTO не должен быть null");
        assertEquals(1L, dto.getId(), "Неверный идентификатор профиля");
        assertEquals(createdAt, dto.getCreatedAt(), "Неверная дата создания");
        assertEquals(updatedAt, dto.getUpdatedAt(), "Неверная дата обновления");

        ClientProfileBasicDto basicDto = dto.getBasic();
        assertNotNull(basicDto, "Базовые данные не должны быть null");
        assertEquals("Acme Corp", basicDto.getCompanyName(), "Неверное название компании");
        assertNull(basicDto.getPosition(), "Поле должности должно быть null, если оно не установлено");
        assertEquals("Россия", basicDto.getCountry(), "Неверная страна");
        assertEquals("Москва", basicDto.getCity(), "Неверный город");
        assertEquals("Ivan", basicDto.getFirstName(), "Неверное имя пользователя");
        assertEquals("Ivanov", basicDto.getLastName(), "Неверная фамилия пользователя");
        assertEquals("user@example.com", basicDto.getEmail(), "Неверный email пользователя");
        assertEquals("+79991234567", basicDto.getPhone(), "Неверный номер телефона пользователя");

        ClientProfileContactDto contactDto = dto.getContact();
        assertNotNull(contactDto, "Контактные данные не должны быть null");
        assertEquals("http://acme-corp.example.com", contactDto.getContactLink(), "Неверная ссылка для контакта");

        ClientProfileFieldDto fieldDto = dto.getField();
        assertNotNull(fieldDto, "Информация о сфере деятельности не должна быть null");
        assertEquals("Информационные технологии", fieldDto.getFieldDescription(), "Неверное описание сферы деятельности");

        UserDto userDto = dto.getUser();
        assertNotNull(userDto, "Данные пользователя не должны быть null");
        assertEquals("Ivan", userDto.getFirstName(), "Неверное имя пользователя");
        assertEquals("Ivanov", userDto.getLastName(), "Неверная фамилия пользователя");
        assertEquals("+79991234567", userDto.getPhone(), "Неверный номер телефона пользователя");
        assertEquals("user@example.com", userDto.getEmail(), "Неверный email пользователя");
        assertNull(dto.getAvatarUrl(), "avatarUrl должен быть null, если не установлен");
        String avatar = "http://example.com/avatar-client.png";
        profile.setAvatarUrl(avatar);
        dto = mapper.toDto(profile);
        assertEquals(avatar, dto.getAvatarUrl(), "avatarUrl должен попадать в DTO");
    }
}
package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.client.ClientProfileBasicDto;
import com.example.jobsyserver.dto.client.ClientProfileContactDto;
import com.example.jobsyserver.dto.client.ClientProfileDto;
import com.example.jobsyserver.dto.client.ClientProfileFieldDto;
import com.example.jobsyserver.dto.user.UserDto;
import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(1L, dto.getId(), "Неверный идентификатор");
        assertEquals(createdAt, dto.getCreatedAt(), "Неверная дата создания");
        assertEquals(updatedAt, dto.getUpdatedAt(), "Неверная дата обновления");

        ClientProfileBasicDto basicDto = dto.getBasic();
        assertNotNull(basicDto, "Базовые данные не должны быть null");
        assertEquals("Acme Corp", basicDto.getCompanyName(), "Неверное название компании");
         assertNull(basicDto.getPosition(), "Поле должности должно быть null, если оно не установлено");

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
        assertEquals("+79991234567", userDto.getPhone(), "Неверный номер телефона");
    }
}
package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileAboutDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileBasicDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileContactDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.user.UserDto;
import com.example.jobsyserver.enums.Experience;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FreelancerProfileMapperTest {

    @Autowired
    private FreelancerProfileMapper mapper;

    @Test
    void testToDto() {
        User user = User.builder()
                .id(1L)
                .email("freelancer@example.com")
                .firstName("Иван")
                .lastName("Иванов")
                .phone("+7999999999")
                .dateBirth(LocalDate.of(1990, 1, 1))
                .build();

        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        FreelancerProfile profile = FreelancerProfile.builder()
                .id(1L)
                .user(user)
                .experienceLevel(Experience.EXPERT)
                .categoryId(2L)
                .specializationId(3L)
                .country("Россия")
                .city("Москва")
                .aboutMe("Фрилансер с опытом в разработке ПО")
                .contactLink("http://portfolio.example.com")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        FreelancerProfileDto dto = mapper.toDto(profile);
        assertNotNull(dto, "Полученный dto не должен быть null");
        assertEquals(1L, dto.getId(), "Неверный идентификатор");
        assertEquals(createdAt, dto.getCreatedAt(), "Неверная дата создания");
        assertEquals(updatedAt, dto.getUpdatedAt(), "Неверная дата обновления");
        FreelancerProfileBasicDto basicDto = dto.getBasic();
        assertNotNull(basicDto, "Базовые данные не должны быть null");
        assertEquals("Россия", basicDto.getCountry(), "Неверная страна");
        assertEquals("Москва", basicDto.getCity(), "Неверный город");
        FreelancerProfileContactDto contactDto = dto.getContact();
        assertNotNull(contactDto, "Контактные данные не должны быть null");
        assertEquals("http://portfolio.example.com", contactDto.getContactLink(), "Неверная ссылка для контакта");
        FreelancerProfileAboutDto aboutDto = dto.getAbout();
        assertNotNull(aboutDto, "Информация о профиле не должна быть null");
        assertEquals(2L, aboutDto.getCategoryId(), "Неверное значение categoryId");
        assertEquals(3L, aboutDto.getSpecializationId(), "Неверное значение specializationId");
        assertEquals(Experience.EXPERT, aboutDto.getExperienceLevel(), "Неверное значение experienceLevel");
        assertEquals("Фрилансер с опытом в разработке ПО", aboutDto.getAboutMe(), "Неверное описание о себе");
        UserDto userDto = dto.getUser();
        assertNotNull(userDto, "Данные пользователя не должны быть null");
        assertEquals("Иван", userDto.getFirstName(), "Неверное имя пользователя");
        assertEquals("Иванов", userDto.getLastName(), "Неверная фамилия пользователя");
        assertEquals("+7999999999", userDto.getPhone(), "Неверный номер телефона");
        assertEquals("freelancer@example.com", userDto.getEmail(), "Неверный email пользователя");
    }
}
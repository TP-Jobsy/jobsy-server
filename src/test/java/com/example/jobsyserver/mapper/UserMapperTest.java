package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.user.UserDto;
import com.example.jobsyserver.enums.UserRole;
import com.example.jobsyserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserToUserDtoMapping() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("secretPassword")
                .role(UserRole.CLIENT)
                .firstName("Иван")
                .lastName("Иванов")
                .dateBirth(LocalDate.of(1990, 1, 1))
                .phone("+7999999999")
                .isVerified(true)
                .verificationCode("1234")
                .isActive(true)
                .createdAt(LocalDateTime.of(2024, 3, 30, 12, 0))
                .updatedAt(LocalDateTime.of(2024, 3, 30, 12, 0))
                .build();

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto, "UserDto не должен быть null");
        assertEquals(user.getEmail(), userDto.getEmail(), "Неверный email пользователя");
        assertEquals(user.getFirstName(), userDto.getFirstName(), "Неверное имя пользователя");
        assertEquals(user.getLastName(), userDto.getLastName(), "Неверная фамилия пользователя");
        assertEquals(user.getPhone(), userDto.getPhone(), "Неверный номер телефона");
        assertEquals(user.getIsVerified(), userDto.getIsVerified(), "Неверное значение флага подтверждения email");
    }
}
package com.example.jobsyserver.model;

import com.example.jobsyserver.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Пользователь системы")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID пользователя", example = "1")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    @Schema(description = "E-mail пользователя", example = "user@example.com")
    private String email;

    @Column(length = 255, nullable = false)
    @Schema(description = "Пароль (не возвращается в ответах)", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Роль пользователя", example = "CLIENT")
    private UserRole role;

    @Column(name = "first_name", length = 50, nullable = false)
    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Column(name = "date_birth", nullable = false)
    @Schema(description = "Дата рождения", example = "2000-01-01")
    private LocalDate dateBirth;

    @Column(length = 30)
    @Schema(description = "Номер телефона", example = "+79991234567")
    private String phone;

    @Column(name = "is_verified", nullable = false)
    @Schema(description = "Флаг подтверждения email", example = "true")
    private Boolean isVerified = false;

    @Column(name = "verification_code", length = 10)
    @Schema(description = "Код подтверждения email", example = "1234", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String verificationCode;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Активен ли пользователь", example = "true")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    @Schema(description = "Дата создания аккаунта", example = "2024-03-30T12:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    @Schema(description = "Дата последнего обновления аккаунта", example = "2024-03-30T12:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();
}


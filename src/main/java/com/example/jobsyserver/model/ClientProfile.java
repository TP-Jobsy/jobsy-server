package com.example.jobsyserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Профиль заказчика")
public class ClientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор профиля заказчика", example = "1")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "Пользователь, которому принадлежит профиль")
    private User user;

    @Column(name = "company_name", length = 100)
    @Schema(description = "Название компании", example = "Альфа")
    private String companyName;

    @Column(name = "country", length = 100)
    @Schema(description = "Страна", example = "Россия")
    private String country;

    @Column(name = "position", length = 100)
    @Schema(description = "Страна", example = "Россия")
    private String position;

    @Column(name = "city", length = 100)
    @Schema(description = "Город", example = "Москва")
    private String city;

    @Column(name = "contact_link", length = 255)
    @Schema(description = "Ссылка для связи или на сайт компании", example = "http://acme-corp.example.com")
    private String contactLink;

    @Column(name = "field_description")
    @Schema(description = "Описание сферы деятельности компании", example = "Информационные технологии")
    private String fieldDescription;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    @Schema(description = "Дата создания профиля", example = "2024-03-30T12:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Дата последнего обновления профиля", example = "2024-03-30T12:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
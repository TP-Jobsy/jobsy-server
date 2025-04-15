package com.example.jobsyserver.model;

import com.example.jobsyserver.enums.Complexity;
import com.example.jobsyserver.enums.PaymentType;
import com.example.jobsyserver.enums.ProjectDuration;
import com.example.jobsyserver.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Проект")
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID проекта", example = "1")
    private Long id;

    @Column(nullable = false, length = 200)
    @Schema(description = "Название проекта", example = "Название проекта")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Описание проекта", example = "Описание проекта...")
    private String description;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Сложность проекта", example = "easy")
    private Complexity projectComplexity;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Тип оплаты", example = "fixed")
    private PaymentType paymentType;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Минимальная ставка", example = "1.01")
    private BigDecimal minRate;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Максимальная ставка", example = "1.01")
    private BigDecimal maxRate;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Фиксированная стоимость", example = "1.01")
    private BigDecimal fixedPrice;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Длительность выполнения проекта", example = "less_than_1_month")
    private ProjectDuration projectDuration;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Статус проекта", example = "open")
    private ProjectStatus status = ProjectStatus.OPEN;

    @CreationTimestamp
    @Schema(description = "Дата создания проекта", example = "2024-03-30T12:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "Дата последнего обновления проекта", example = "2024-03-30T12:00:00")
    private LocalDateTime updatedAt;
}

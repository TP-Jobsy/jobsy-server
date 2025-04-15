package com.example.jobsyserver.model;

import com.example.jobsyserver.enums.ProjectApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_applications")
public class ProjectApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный ID заявки", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @Schema(description = "Уникальный ID проекта", example = "1")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    @Schema(description = "Уникальный ID фрилансера", example = "1")
    private User freelancer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "Статус заявки", example = "pending")
    private ProjectApplicationStatus status = ProjectApplicationStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Schema(description = "Дата создания заявки", example = "2024-03-30T12:00:00")
    private LocalDateTime createdAt;
}

package com.example.jobsyserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Навык, применяемый в проектах")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор навыка", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Название навыка", example = "JavaScript")
    private String name;
}
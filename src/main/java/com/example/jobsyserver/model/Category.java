package com.example.jobsyserver.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Категория проектов")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор категории", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Название категории", example = "Web Development")
    private String name;
}

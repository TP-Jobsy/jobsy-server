package com.example.jobsyserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite_projects",
        uniqueConstraints = @UniqueConstraint(columnNames = {"freelancer_id","project_id"}))
public class FavoriteProject {
    @Id
    @GeneratedValue private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerProfile freelancer;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

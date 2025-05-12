package com.example.jobsyserver.features.favorites.model;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
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

    @EmbeddedId
    private FavoriteProjectId id;

    @MapsId("freelancerId")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerProfile freelancer;

    @MapsId("projectId")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

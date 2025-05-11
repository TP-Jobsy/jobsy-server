package com.example.jobsyserver.features.favorites.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProjectId implements Serializable {

    @Column(name = "freelancer_id")
    private Long freelancerId;

    @Column(name = "project_id")
    private Long projectId;
}
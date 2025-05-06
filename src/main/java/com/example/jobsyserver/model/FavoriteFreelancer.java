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
@Table(name = "favorite_freelancers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id","freelancer_id"}))
public class FavoriteFreelancer {

    @EmbeddedId
    private FavoriteFreelancerId id;

    @MapsId("clientId")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientProfile client;

    @MapsId("freelancerId")
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerProfile freelancer;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

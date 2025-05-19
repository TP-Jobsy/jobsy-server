package com.example.jobsyserver.features.rating.model;

import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rater_freelancer_id")
    private FreelancerProfile raterFreelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rater_client_id")
    private ClientProfile raterClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_freelancer_id")
    private FreelancerProfile targetFreelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_client_id")
    private ClientProfile targetClient;

    @Column(nullable = false)
    private Integer score;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

package com.example.jobsyserver.features.freelancer.model;

import com.example.jobsyserver.features.common.enums.Experience;
import com.example.jobsyserver.features.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "freelancer_profiles")
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", columnDefinition = "experience_enum")
    private Experience experienceLevel;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "contact_link", length = 255)
    private String contactLink;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "freelancerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FreelancerSkill> freelancerSkills = new ArrayList<>();

    @Column(name="avatar_url")
    private String avatarUrl;

    @Column(name = "average_rating", nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(name = "rating_count",   nullable = false)
    @Builder.Default
    private Integer ratingCount   = 0;
}
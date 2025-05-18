package com.example.jobsyserver.features.client.model;

import com.example.jobsyserver.features.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "company_name", length = 100)
    private String companyName;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "contact_link", length = 255)
    private String contactLink;

    @Column(name = "field_description")
    private String fieldDescription;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name="avatar_url")
    private String avatarUrl;

    @Column(name = "average_rating", nullable = false)
    private Double averageRating = 0.0;

    @Column(name = "rating_count",   nullable = false)
    private Integer ratingCount   = 0;
}
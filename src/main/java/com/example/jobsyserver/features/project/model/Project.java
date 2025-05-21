package com.example.jobsyserver.features.project.model;

import com.example.jobsyserver.features.common.enums.*;
import com.example.jobsyserver.features.category.model.Category;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.specialization.model.Specialization;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedEntityGraph(
        name = "Project.full",
        attributeNodes = {
                @NamedAttributeNode("category"),
                @NamedAttributeNode("specialization"),
                @NamedAttributeNode(value = "client", subgraph = "client-user"),
                @NamedAttributeNode(value = "assignedFreelancer", subgraph = "freelancer-user"),
                @NamedAttributeNode("skills")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "client-user",
                        attributeNodes = @NamedAttributeNode("user")
                ),
                @NamedSubgraph(
                        name = "freelancer-user",
                        attributeNodes = @NamedAttributeNode("user")
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientProfile client;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity", nullable = false)
    private Complexity projectComplexity;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "fixed_price", precision = 10, scale = 2)
    private BigDecimal fixedPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration")
    private ProjectDuration projectDuration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_freelancer_id")
    private FreelancerProfile assignedFreelancer;

    @ManyToMany
    @JoinTable(name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @BatchSize(size = 20)
    @Builder.Default
    private Set<Skill> skills = new HashSet<>();

    @Column(name = "client_completed", nullable = false)
    @Builder.Default
    private boolean clientCompleted = false;

    @Column(name = "freelancer_completed", nullable = false)
    @Builder.Default
    private boolean freelancerCompleted = false;
}

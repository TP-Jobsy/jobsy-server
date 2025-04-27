package com.example.jobsyserver.model;

import com.example.jobsyserver.enums.ConfirmationAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "confirmations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Confirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_confirmation_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, columnDefinition = "confirmation_action_enum")
    private ConfirmationAction action;

    @Column(name = "confirmation_code", nullable = false, length = 10)
    private String confirmationCode;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    @Column(name = "used", nullable = false)
    private Boolean used = false;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
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
public class FavoriteFreelancerId implements Serializable {

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "freelancer_id")
    private Long freelancerId;
}

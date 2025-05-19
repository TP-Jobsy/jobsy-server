package com.example.jobsyserver.features.client.repository;

import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUser(User user);
    Optional<ClientProfile> findByUserEmail(String email);

}
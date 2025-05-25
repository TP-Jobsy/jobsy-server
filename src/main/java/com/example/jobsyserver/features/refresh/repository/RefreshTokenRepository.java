package com.example.jobsyserver.features.refresh.repository;

import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteAllByUser(User user);

    long deleteAllByExpiryDateBeforeOrRevokedTrue(Instant now);
}

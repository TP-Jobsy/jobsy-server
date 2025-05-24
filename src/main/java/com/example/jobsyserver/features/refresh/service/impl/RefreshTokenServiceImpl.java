package com.example.jobsyserver.features.refresh.service.impl;

import com.example.jobsyserver.features.common.config.jwt.JwtProperties;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.refresh.repository.RefreshTokenRepository;
import com.example.jobsyserver.features.refresh.service.RefreshTokenService;
import com.example.jobsyserver.features.user.model.User;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final JwtProperties jwtProperties;

    @Override
    public RefreshToken createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusMillis(jwtProperties.refreshExpiration().toMillis());
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiry)
                .revoked(false)
                .build();
        return repo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyAndGet(String token) {
        RefreshToken refreshToken = repo.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh токен", token));
        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new JwtException("Refresh токен недействителен или истек");
        }
        return refreshToken;
    }

    @Override
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repo.save(token);
    }

    @Override
    public void revokeAllForUser(User user) {
        repo.deleteAllByUser(user);
    }

    @Override
    public RefreshToken rotateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtProperties.refreshExpiration().toMillis()));
        refreshToken.setRevoked(false);
        return repo.save(refreshToken);
    }
}

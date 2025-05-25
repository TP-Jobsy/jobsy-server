package com.example.jobsyserver.features.refresh.service;

import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.user.model.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyAndGet(String token);
    void revoke(RefreshToken token);
    void revokeAllForUser(User user);
    RefreshToken rotateRefreshToken(RefreshToken refreshToken);

}

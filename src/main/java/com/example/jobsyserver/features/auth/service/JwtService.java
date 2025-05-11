package com.example.jobsyserver.features.auth.service;

public interface JwtService {
    String generateToken(String email);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}

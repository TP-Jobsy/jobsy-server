package com.example.jobsyserver.features.common.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, Duration expiration, Duration refreshExpiration)
{}

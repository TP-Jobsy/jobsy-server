package com.example.jobsyserver.features.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.DisabledException;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        if (exception instanceof DisabledException) {
            message = "Ваша учётная запись заблокирована";
        } else {
            message = "Неверные учётные данные";
        }
        mapper.writeValue(response.getOutputStream(), Map.of(
                "status", HttpStatus.UNAUTHORIZED.value(),
                "error", message
        ));
    }
}

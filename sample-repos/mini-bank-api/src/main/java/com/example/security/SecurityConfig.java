package com.example.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    
    private final JwtService jwtService;
    
    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}

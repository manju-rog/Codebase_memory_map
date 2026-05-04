package com.example.security;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    
    public String generateToken(String username) {
        // Generate JWT token
        return "jwt-token-for-" + username;
    }
    
    public boolean validateToken(String token) {
        return token != null && token.startsWith("jwt-token");
    }
}

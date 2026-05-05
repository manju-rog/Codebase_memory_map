package com.example.auth;

public class TokenService {
    public String generateToken(User user) {
        // JWT token generation logic
        return "jwt_token_" + user.getUsername();
    }
}

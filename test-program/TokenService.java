package com.example.auth;

import java.time.LocalDateTime;
import java.util.UUID;
import java.security.SecureRandom;

public class TokenService {
    
    private static final int TOKEN_LENGTH = 32;
    private static final int TOKEN_EXPIRY_HOURS = 24;
    
    /**
     * Generate JWT token for authenticated user
     */
    public String generateToken(User user) {
        // In real implementation, this would create a proper JWT with:
        // - Header (algorithm and token type)
        // - Payload (user claims: username, role, expiry)
        // - Signature (HMAC SHA256)
        
        String header = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"; // Base64 encoded header
        String payload = createPayload(user);
        String signature = createSignature(header, payload);
        
        return header + "." + payload + "." + signature;
    }
    
    /**
     * Generate password reset token
     */
    public static String generateResetToken() {
        return generateRandomToken(TOKEN_LENGTH);
    }
    
    /**
     * Generate refresh token
     */
    public String generateRefreshToken(User user) {
        return "refresh_" + generateRandomToken(TOKEN_LENGTH) + "_" + user.getUsername();
    }
    
    /**
     * Validate token
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        
        // In real implementation, verify signature and expiry
        return true;
    }
    
    /**
     * Extract username from token
     */
    public String getUsernameFromToken(String token) {
        if (!validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }
        
        // In real implementation, decode JWT payload and extract username
        String[] parts = token.split("\\.");
        String payload = parts[1];
        
        // Simulated extraction
        return "user_from_token";
    }
    
    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        if (!validateToken(token)) {
            return true;
        }
        
        // In real implementation, check exp claim in JWT
        return false;
    }
    
    /**
     * Refresh access token
     */
    public String refreshAccessToken(String refreshToken, User user) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        return generateToken(user);
    }
    
    /**
     * Revoke token (add to blacklist)
     */
    public void revokeToken(String token) {
        // In real implementation, add token to Redis blacklist
        System.out.println("Token revoked: " + token.substring(0, 20) + "...");
    }
    
    /**
     * Create JWT payload
     */
    private String createPayload(User user) {
        // In real implementation, create JSON with claims and Base64 encode
        // Claims: sub (username), role, iat (issued at), exp (expiry)
        
        long issuedAt = System.currentTimeMillis() / 1000;
        long expiry = issuedAt + (TOKEN_EXPIRY_HOURS * 3600);
        
        String json = String.format(
            "{\"sub\":\"%s\",\"role\":\"%s\",\"iat\":%d,\"exp\":%d}",
            user.getUsername(),
            user.getRole(),
            issuedAt,
            expiry
        );
        
        return base64Encode(json);
    }
    
    /**
     * Create JWT signature
     */
    private String createSignature(String header, String payload) {
        // In real implementation, use HMAC SHA256 with secret key
        String data = header + "." + payload;
        return base64Encode("signature_" + data.hashCode());
    }
    
    /**
     * Generate random token
     */
    private static String generateRandomToken(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            token.append(chars.charAt(index));
        }
        
        return token.toString();
    }
    
    /**
     * Simple Base64 encoding simulation
     */
    private String base64Encode(String input) {
        return java.util.Base64.getEncoder().encodeToString(input.getBytes());
    }
}

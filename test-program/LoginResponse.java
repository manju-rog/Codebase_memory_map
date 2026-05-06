package com.example.auth;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;
    private long expiresIn;
    
    public LoginResponse(String accessToken, String refreshToken, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;
        this.expiresIn = 86400; // 24 hours in seconds
    }
    
    // Legacy constructor for backward compatibility
    public LoginResponse(String token, String username) {
        this(token, null, username, "USER");
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRole() {
        return role;
    }
    
    public long getExpiresIn() {
        return expiresIn;
    }
    
    public String getTokenType() {
        return "Bearer";
    }
}

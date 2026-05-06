package com.example.auth;

import java.time.LocalDateTime;

public class User {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role;
    private boolean active;
    private String resetToken;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private int loginAttempts;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.loginAttempts = 0;
        this.role = "USER";
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getRole() {
        return role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public String getResetToken() {
        return resetToken;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public int getLoginAttempts() {
        return loginAttempts;
    }
    
    // Setters
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }
    
    // Business methods
    public void incrementLoginAttempts() {
        this.loginAttempts++;
    }
    
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
    }
    
    public boolean isLocked() {
        return this.loginAttempts >= 5;
    }
    
    public boolean hasRole(String role) {
        return this.role != null && this.role.equals(role);
    }
}

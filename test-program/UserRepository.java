package com.example.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserRepository {
    
    private Map<String, User> users = new HashMap<>();
    private Map<String, User> usersByEmail = new HashMap<>();
    
    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        return users.get(username);
    }
    
    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }
    
    /**
     * Find users by role
     */
    public List<User> findByRole(String role) {
        return users.values().stream()
            .filter(user -> user.hasRole(role))
            .collect(Collectors.toList());
    }
    
    /**
     * Find all active users
     */
    public List<User> findActiveUsers() {
        return users.values().stream()
            .filter(User::isActive)
            .collect(Collectors.toList());
    }
    
    /**
     * Find all users
     */
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Save new user
     */
    public void save(User user) {
        if (user == null || user.getUsername() == null) {
            throw new RuntimeException("Invalid user");
        }
        
        users.put(user.getUsername(), user);
        
        if (user.getEmail() != null) {
            usersByEmail.put(user.getEmail(), user);
        }
    }
    
    /**
     * Update existing user
     */
    public void update(User user) {
        if (user == null || user.getUsername() == null) {
            throw new RuntimeException("Invalid user");
        }
        
        if (!users.containsKey(user.getUsername())) {
            throw new RuntimeException("User not found");
        }
        
        users.put(user.getUsername(), user);
        
        if (user.getEmail() != null) {
            usersByEmail.put(user.getEmail(), user);
        }
    }
    
    /**
     * Delete user
     */
    public void delete(String username) {
        User user = users.remove(username);
        
        if (user != null && user.getEmail() != null) {
            usersByEmail.remove(user.getEmail());
        }
    }
    
    /**
     * Check if username exists
     */
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }
    
    /**
     * Check if email exists
     */
    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email);
    }
    
    /**
     * Count total users
     */
    public int count() {
        return users.size();
    }
    
    /**
     * Count active users
     */
    public int countActive() {
        return (int) users.values().stream()
            .filter(User::isActive)
            .count();
    }
}

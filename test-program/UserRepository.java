package com.example.auth;

public class UserRepository {
    public User findByUsername(String username) {
        // Database lookup logic
        return new User(username, "hashedPassword");
    }
}

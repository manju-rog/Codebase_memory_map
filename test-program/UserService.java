package com.example.auth;

import java.util.List;
import java.util.ArrayList;

public class UserService {
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    
    public UserService() {
        this.userRepository = new UserRepository();
        this.passwordEncoder = new PasswordEncoder();
        this.emailService = new EmailService();
    }
    
    /**
     * Register a new user
     */
    public User registerUser(String username, String email, String password, String role) {
        // Validate input
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        
        if (!isValidEmail(email)) {
            throw new RuntimeException("Invalid email format");
        }
        
        // Check if user already exists
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        
        // Hash password
        String hashedPassword = passwordEncoder.encode(password);
        
        // Create user
        User newUser = new User(username, hashedPassword);
        newUser.setEmail(email);
        newUser.setRole(role);
        newUser.setActive(true);
        
        // Save to repository
        userRepository.save(newUser);
        
        // Send welcome email
        emailService.sendWelcomeEmail(email, username);
        
        return newUser;
    }
    
    /**
     * Update user profile
     */
    public User updateProfile(String username, String email, String fullName) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        
        if (fullName != null && !fullName.isEmpty()) {
            user.setFullName(fullName);
        }
        
        userRepository.update(user);
        
        return user;
    }
    
    /**
     * Change user password
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        
        // Validate new password strength
        if (!isStrongPassword(newPassword)) {
            throw new RuntimeException("Password must be at least 8 characters with uppercase, lowercase, and numbers");
        }
        
        // Hash and update password
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        
        userRepository.update(user);
        
        // Send notification email
        emailService.sendPasswordChangedEmail(user.getEmail(), username);
    }
    
    /**
     * Deactivate user account
     */
    public void deactivateUser(String username) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        user.setActive(false);
        userRepository.update(user);
        
        emailService.sendAccountDeactivatedEmail(user.getEmail(), username);
    }
    
    /**
     * Get all users by role
     */
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Get all active users
     */
    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }
    
    /**
     * Reset password (forgot password flow)
     */
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            // Don't reveal if email exists for security
            return;
        }
        
        // Generate reset token
        String resetToken = TokenService.generateResetToken();
        user.setResetToken(resetToken);
        
        userRepository.update(user);
        
        // Send reset email
        emailService.sendPasswordResetEmail(email, resetToken);
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    /**
     * Check password strength
     */
    private boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
}

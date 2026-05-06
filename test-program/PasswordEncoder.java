package com.example.auth;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncoder {
    
    private static final int SALT_LENGTH = 16;
    
    /**
     * Encode password with salt
     */
    public String encode(String rawPassword) {
        try {
            // Generate salt
            byte[] salt = generateSalt();
            
            // Hash password with salt
            String hash = hashPassword(rawPassword, salt);
            
            // Combine salt and hash
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            
            return saltBase64 + ":" + hash;
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode password", e);
        }
    }
    
    /**
     * Check if raw password matches encoded password
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            // Split salt and hash
            String[] parts = encodedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String saltBase64 = parts[0];
            String storedHash = parts[1];
            
            // Decode salt
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            
            // Hash the raw password with the same salt
            String computedHash = hashPassword(rawPassword, salt);
            
            // Compare hashes
            return computedHash.equals(storedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate random salt
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * Hash password with salt using SHA-256
     */
    private String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        // Add salt to digest
        md.update(salt);
        
        // Hash password
        byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));
        
        // Convert to Base64
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
    
    /**
     * Generate strong random password
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
}

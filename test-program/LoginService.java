package com.example.auth;

import java.time.LocalDateTime;

public class LoginService {
    
    private UserRepository userRepository;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private AuditLogger auditLogger;
    
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    
    public LoginService() {
        this.userRepository = new UserRepository();
        this.tokenService = new TokenService();
        this.passwordEncoder = new PasswordEncoder();
        this.emailService = new EmailService();
        this.auditLogger = new AuditLogger();
    }
    
    /**
     * Main login method
     */
    public LoginResponse login(String username, String password) {
        return login(username, password, "127.0.0.1");
    }
    
    /**
     * Login with IP tracking
     */
    public LoginResponse login(String username, String password, String ipAddress) {
        // Step 1: Find user
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            auditLogger.logFailedLogin(username, ipAddress, "User not found");
            throw new RuntimeException("Invalid username or password");
        }
        
        // Step 2: Check if account is active
        if (!user.isActive()) {
            auditLogger.logFailedLogin(username, ipAddress, "Account deactivated");
            throw new RuntimeException("Account is deactivated");
        }
        
        // Step 3: Check if account is locked
        if (user.isLocked()) {
            auditLogger.logFailedLogin(username, ipAddress, "Account locked");
            throw new RuntimeException("Account is locked due to too many failed attempts");
        }
        
        // Step 4: Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.incrementLoginAttempts();
            userRepository.update(user);
            
            auditLogger.logFailedLogin(username, ipAddress, "Invalid password");
            
            if (user.isLocked()) {
                emailService.sendAccountDeactivatedEmail(user.getEmail(), username);
            }
            
            throw new RuntimeException("Invalid username or password");
        }
        
        // Step 5: Reset login attempts on successful login
        user.resetLoginAttempts();
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.update(user);
        
        // Step 6: Generate tokens
        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        
        // Step 7: Log successful login
        auditLogger.logSuccessfulLogin(username, ipAddress);
        
        // Step 8: Send login notification
        emailService.sendLoginNotification(user.getEmail(), username, ipAddress);
        
        // Step 9: Return response
        return new LoginResponse(accessToken, refreshToken, user.getUsername(), user.getRole());
    }
    
    /**
     * Logout user
     */
    public void logout(String token) {
        // Revoke token
        tokenService.revokeToken(token);
        
        // Log logout
        String username = tokenService.getUsernameFromToken(token);
        auditLogger.logLogout(username);
    }
    
    /**
     * Refresh access token
     */
    public LoginResponse refreshToken(String refreshToken) {
        if (!tokenService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        String username = tokenService.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username);
        
        if (user == null || !user.isActive()) {
            throw new RuntimeException("User not found or inactive");
        }
        
        String newAccessToken = tokenService.refreshAccessToken(refreshToken, user);
        
        return new LoginResponse(newAccessToken, refreshToken, user.getUsername(), user.getRole());
    }
    
    /**
     * Validate session
     */
    public boolean validateSession(String token) {
        if (!tokenService.validateToken(token)) {
            return false;
        }
        
        if (tokenService.isTokenExpired(token)) {
            return false;
        }
        
        String username = tokenService.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);
        
        return user != null && user.isActive();
    }
}

package com.example.auth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailService {
    
    private static final String FROM_EMAIL = "noreply@repolens.ai";
    
    /**
     * Send welcome email to new user
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        String subject = "Welcome to RepoLens AI!";
        String body = buildWelcomeEmailBody(username);
        
        sendEmail(toEmail, subject, body);
        
        logEmail("WELCOME", toEmail, username);
    }
    
    /**
     * Send password changed notification
     */
    public void sendPasswordChangedEmail(String toEmail, String username) {
        String subject = "Password Changed Successfully";
        String body = buildPasswordChangedBody(username);
        
        sendEmail(toEmail, subject, body);
        
        logEmail("PASSWORD_CHANGED", toEmail, username);
    }
    
    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String subject = "Reset Your Password";
        String body = buildPasswordResetBody(resetToken);
        
        sendEmail(toEmail, subject, body);
        
        logEmail("PASSWORD_RESET", toEmail, "");
    }
    
    /**
     * Send account deactivated notification
     */
    public void sendAccountDeactivatedEmail(String toEmail, String username) {
        String subject = "Account Deactivated";
        String body = buildAccountDeactivatedBody(username);
        
        sendEmail(toEmail, subject, body);
        
        logEmail("ACCOUNT_DEACTIVATED", toEmail, username);
    }
    
    /**
     * Send login notification
     */
    public void sendLoginNotification(String toEmail, String username, String ipAddress) {
        String subject = "New Login Detected";
        String body = buildLoginNotificationBody(username, ipAddress);
        
        sendEmail(toEmail, subject, body);
        
        logEmail("LOGIN_NOTIFICATION", toEmail, username);
    }
    
    /**
     * Send email (simulated)
     */
    private void sendEmail(String to, String subject, String body) {
        // In real implementation, this would use SMTP or email service API
        System.out.println("=== EMAIL SENT ===");
        System.out.println("From: " + FROM_EMAIL);
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("==================");
    }
    
    /**
     * Build welcome email body
     */
    private String buildWelcomeEmailBody(String username) {
        return String.format(
            "Hello %s,\n\n" +
            "Welcome to RepoLens AI! Your account has been created successfully.\n\n" +
            "You can now start analyzing your codebases with AI-powered insights.\n\n" +
            "Best regards,\n" +
            "RepoLens AI Team",
            username
        );
    }
    
    /**
     * Build password changed email body
     */
    private String buildPasswordChangedBody(String username) {
        return String.format(
            "Hello %s,\n\n" +
            "Your password has been changed successfully at %s.\n\n" +
            "If you did not make this change, please contact support immediately.\n\n" +
            "Best regards,\n" +
            "RepoLens AI Team",
            username,
            getCurrentTimestamp()
        );
    }
    
    /**
     * Build password reset email body
     */
    private String buildPasswordResetBody(String resetToken) {
        return String.format(
            "Hello,\n\n" +
            "You requested to reset your password.\n\n" +
            "Your reset token is: %s\n\n" +
            "This token will expire in 1 hour.\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            "RepoLens AI Team",
            resetToken
        );
    }
    
    /**
     * Build account deactivated email body
     */
    private String buildAccountDeactivatedBody(String username) {
        return String.format(
            "Hello %s,\n\n" +
            "Your account has been deactivated at %s.\n\n" +
            "If you wish to reactivate your account, please contact support.\n\n" +
            "Best regards,\n" +
            "RepoLens AI Team",
            username,
            getCurrentTimestamp()
        );
    }
    
    /**
     * Build login notification email body
     */
    private String buildLoginNotificationBody(String username, String ipAddress) {
        return String.format(
            "Hello %s,\n\n" +
            "A new login was detected on your account at %s.\n\n" +
            "IP Address: %s\n\n" +
            "If this was not you, please change your password immediately.\n\n" +
            "Best regards,\n" +
            "RepoLens AI Team",
            username,
            getCurrentTimestamp(),
            ipAddress
        );
    }
    
    /**
     * Log email sending
     */
    private void logEmail(String type, String toEmail, String username) {
        System.out.println(String.format(
            "[%s] Email sent: type=%s, to=%s, user=%s",
            getCurrentTimestamp(),
            type,
            toEmail,
            username
        ));
    }
    
    /**
     * Get current timestamp
     */
    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}

package com.example.auth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AuditLogger {
    
    private List<AuditLog> logs = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Log successful login
     */
    public void logSuccessfulLogin(String username, String ipAddress) {
        AuditLog log = new AuditLog(
            "LOGIN_SUCCESS",
            username,
            ipAddress,
            "User logged in successfully"
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Log failed login
     */
    public void logFailedLogin(String username, String ipAddress, String reason) {
        AuditLog log = new AuditLog(
            "LOGIN_FAILED",
            username,
            ipAddress,
            "Login failed: " + reason
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Log logout
     */
    public void logLogout(String username) {
        AuditLog log = new AuditLog(
            "LOGOUT",
            username,
            "N/A",
            "User logged out"
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Log password change
     */
    public void logPasswordChange(String username, String ipAddress) {
        AuditLog log = new AuditLog(
            "PASSWORD_CHANGE",
            username,
            ipAddress,
            "Password changed"
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Log user registration
     */
    public void logUserRegistration(String username, String ipAddress) {
        AuditLog log = new AuditLog(
            "USER_REGISTERED",
            username,
            ipAddress,
            "New user registered"
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Log account deactivation
     */
    public void logAccountDeactivation(String username, String reason) {
        AuditLog log = new AuditLog(
            "ACCOUNT_DEACTIVATED",
            username,
            "N/A",
            "Account deactivated: " + reason
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Log security event
     */
    public void logSecurityEvent(String eventType, String username, String details) {
        AuditLog log = new AuditLog(
            "SECURITY_" + eventType,
            username,
            "N/A",
            details
        );
        
        logs.add(log);
        printLog(log);
    }
    
    /**
     * Get all logs
     */
    public List<AuditLog> getAllLogs() {
        return new ArrayList<>(logs);
    }
    
    /**
     * Get logs by username
     */
    public List<AuditLog> getLogsByUsername(String username) {
        List<AuditLog> userLogs = new ArrayList<>();
        for (AuditLog log : logs) {
            if (log.getUsername().equals(username)) {
                userLogs.add(log);
            }
        }
        return userLogs;
    }
    
    /**
     * Get logs by event type
     */
    public List<AuditLog> getLogsByEventType(String eventType) {
        List<AuditLog> typeLogs = new ArrayList<>();
        for (AuditLog log : logs) {
            if (log.getEventType().equals(eventType)) {
                typeLogs.add(log);
            }
        }
        return typeLogs;
    }
    
    /**
     * Print log to console
     */
    private void printLog(AuditLog log) {
        System.out.println(String.format(
            "[AUDIT] %s | %s | %s | %s | %s",
            log.getTimestamp().format(formatter),
            log.getEventType(),
            log.getUsername(),
            log.getIpAddress(),
            log.getDetails()
        ));
    }
    
    /**
     * Inner class for audit log entry
     */
    public static class AuditLog {
        private String eventType;
        private String username;
        private String ipAddress;
        private String details;
        private LocalDateTime timestamp;
        
        public AuditLog(String eventType, String username, String ipAddress, String details) {
            this.eventType = eventType;
            this.username = username;
            this.ipAddress = ipAddress;
            this.details = details;
            this.timestamp = LocalDateTime.now();
        }
        
        public String getEventType() {
            return eventType;
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getIpAddress() {
            return ipAddress;
        }
        
        public String getDetails() {
            return details;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}

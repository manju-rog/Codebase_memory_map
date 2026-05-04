package com.example.auth;

import com.example.user.UserRepository;
import com.example.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;
    
    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
    
    public LoginResponse login(LoginRequest request) {
        // Authenticate user
        String token = jwtService.generateToken(request.getUsername());
        return new LoginResponse(token);
    }
}

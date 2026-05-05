package com.example.auth;

public class LoginService {
    
    private UserRepository userRepository;
    private TokenService tokenService;
    
    public LoginResponse login(String username, String password) {
        // Step 1: Find user
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        // Step 2: Verify password
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        
        // Step 3: Generate token
        String token = tokenService.generateToken(user);
        
        return new LoginResponse(token, user.getUsername());
    }
}

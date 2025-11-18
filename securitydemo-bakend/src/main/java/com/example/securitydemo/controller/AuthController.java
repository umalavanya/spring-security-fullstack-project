package com.example.securitydemo.controller;

import com.example.securitydemo.dto.RegisterRequest;
import com.example.securitydemo.model.Role;
import com.example.securitydemo.model.User;
import com.example.securitydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getEmail()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser() {
        // For now, this will just return a success message
        // Spring Security handles the actual authentication
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Auth endpoint is working!");
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        // This is just for debugging - remove in production
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser() {
        try {
            // Check if admin already exists
            if (userService.findByUsername("admin").isPresent()) {
                return ResponseEntity.badRequest().body("Admin user already exists");
            }

            User admin = userService.registerUser("admin", "admin123", "admin@example.com");
            // Manually set admin role
            admin.setRole(Role.ADMIN);
            userService.saveUser(admin);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin user created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
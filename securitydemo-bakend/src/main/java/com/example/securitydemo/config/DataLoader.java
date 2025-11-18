package com.example.securitydemo.config;

import com.example.securitydemo.model.Role;
import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create a test user if it doesn't exist
        Optional<User> existingUser = userRepository.findByUsername("testuser");
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setUsername("testuser");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setEmail("test@example.com");
            user.setRole(Role.USER);
            userRepository.save(user);
            System.out.println("Test user created: testuser / password123");
        }

        // Create an admin user if it doesn't exist
        Optional<User> existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created: admin / admin123");
        }
    }
}
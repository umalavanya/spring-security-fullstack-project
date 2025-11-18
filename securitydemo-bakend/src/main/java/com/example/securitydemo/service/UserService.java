package com.example.securitydemo.service;

import com.example.securitydemo.model.User;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user.get();
    }

    public User registerUser(String username, String password, String email) {
        try {
            System.out.println("Attempting to register user: " + username);

            // Check if user already exists
            if (userRepository.existsByUsername(username)) {
                System.out.println("Username already exists: " + username);
                throw new RuntimeException("Username already exists");
            }
            if (userRepository.existsByEmail(email)) {
                System.out.println("Email already exists: " + email);
                throw new RuntimeException("Email already exists");
            }

            // Create new user
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRole(com.example.securitydemo.model.Role.USER);

            User savedUser = userRepository.save(user);
            System.out.println("User registered successfully: " + savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
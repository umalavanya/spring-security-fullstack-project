package com.example.securitydemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/secured")
    public ResponseEntity<?> securedEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a secured endpoint");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> adminEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is an admin-only endpoint");
        return ResponseEntity.ok(response);
    }
}
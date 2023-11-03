package com.example.clothesshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.clothesshop.service.JwtService;

@RestController
public class JwtController {

    private JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/api/token/isValid")
    public ResponseEntity<?> isTokenValid(@RequestHeader(value = "Authorization") String tokenHeader) {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);

            boolean isValid = jwtService.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok("Token is valid.");
            } else {
                return ResponseEntity.badRequest().body("Token is expired or invalid.");
            }
        }
        return ResponseEntity.badRequest().body("Token is missing.");
    }
}

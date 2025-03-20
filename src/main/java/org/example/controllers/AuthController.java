package org.example.controllers;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.example.security.JwtService;

@RestController
public class AuthController {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        var userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails.getPassword().equals("{noop}" + password)) {
            return jwtService.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, secured world!";
    }
}


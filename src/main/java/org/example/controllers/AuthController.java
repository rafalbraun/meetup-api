package org.example.controllers;

import jakarta.validation.Valid;
import org.example.Constants;
import org.example.dto.UserDto;
import org.example.exceptions.ValidationException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.example.security.JwtService;
import org.example.repository.UserRepository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.example.Constants.TEST_STRING;
import static org.example.exceptions.ExceptionMessages.INVALID_CREDENTIALS;

@RestController
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(JwtService jwtService, UserService userService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping(Constants.LOGIN_URL)
    public String login(@RequestParam String username, @RequestParam String password) {
        var userDetails = userService.loadUserByUsername(username);
        if (userDetails.getPassword().equals(password)) {
            return jwtService.generateToken(username);
        } else {
            throw new RuntimeException(INVALID_CREDENTIALS);
        }
    }

    @PostMapping(Constants.REGISTER)
    public ResponseEntity<?> register(@RequestBody @Valid UserDto request) throws ValidationException {
        UserDto saved = userService.registerNewUser(request);
        URI userUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(userUri).body(saved);
    }

    @GetMapping(Constants.TEST)
    public String test() {
        return TEST_STRING;
    }

    @GetMapping(Constants.ME_URL)
    public UserDto getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserMapper.toDto(user);
    }

    @GetMapping(Constants.GET_USER)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserbyId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}


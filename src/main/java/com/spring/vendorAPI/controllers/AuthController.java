package com.spring.vendorAPI.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.vendorAPI.dtos.LoginRequestDto;
import com.spring.vendorAPI.dtos.RegisterRequestDto;
import com.spring.vendorAPI.entities.User;
import com.spring.vendorAPI.services.interfaces.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        try {
            User user = new User();
            user.setEmail(registerRequestDto.getEmail());
            user.setPassword(registerRequestDto.getPassword());

            User registeredUser = authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("data", registeredUser));
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Collections.singletonMap("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            String email = loginRequestDto.getEmail();
            String password = loginRequestDto.getPassword();
            Map<String, Object> response = authService.login(email, password);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, Object> errorResponse = Collections.singletonMap("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
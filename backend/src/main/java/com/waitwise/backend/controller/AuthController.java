package com.waitwise.backend.controller;

import com.waitwise.backend.dto.auth.AuthResponse;
import com.waitwise.backend.dto.auth.LoginRequest;
import com.waitwise.backend.dto.auth.RegisterRequest;
import com.waitwise.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
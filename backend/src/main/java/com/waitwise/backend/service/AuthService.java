package com.waitwise.backend.service;

import com.waitwise.backend.dto.auth.AuthResponse;
import com.waitwise.backend.dto.auth.LoginRequest;
import com.waitwise.backend.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
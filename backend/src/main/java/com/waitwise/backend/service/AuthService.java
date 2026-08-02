package com.waitwise.backend.service;

import com.waitwise.backend.dto.AuthResponse;
import com.waitwise.backend.dto.LoginRequest;
import com.waitwise.backend.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
package com.waitwise.backend.service;

import com.waitwise.backend.dto.AuthResponse;
import com.waitwise.backend.dto.LoginRequest;
import com.waitwise.backend.dto.RegisterRequest;
import com.waitwise.backend.dto.UserResponse;
import com.waitwise.backend.entity.User;
import com.waitwise.backend.enums.Role;
import com.waitwise.backend.repository.UserRepository;
import com.waitwise.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        com.waitwise.backend.entity.User user = com.waitwise.backend.entity.User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return new AuthResponse(token, response);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
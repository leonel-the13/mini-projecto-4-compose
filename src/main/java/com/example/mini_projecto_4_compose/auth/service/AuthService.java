package com.example.mini_projecto_4_compose.auth.service;

import com.example.mini_projecto_4_compose.auth.repository.UserRepository;
import com.example.mini_projecto_4_compose.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
}

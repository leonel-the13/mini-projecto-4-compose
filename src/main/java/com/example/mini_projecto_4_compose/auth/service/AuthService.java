package com.example.mini_projecto_4_compose.auth.service;

import com.example.mini_projecto_4_compose.auth.dto.AuthResponse;
import com.example.mini_projecto_4_compose.auth.dto.LoginRequest;
import com.example.mini_projecto_4_compose.auth.dto.RegisterRequest;
import com.example.mini_projecto_4_compose.auth.model.User;
import com.example.mini_projecto_4_compose.auth.repository.UserRepository;
import com.example.mini_projecto_4_compose.auth.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;

@Service
public class AuthService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtService jwtService;

    public void register(RegisterRequest request) {
        String hashed = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashed);

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}

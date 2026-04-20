package com.example.mini_projecto_4_compose.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {
    private final String SECRET = "minha-super-chave-secreta";

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600))
                .signWith(SignatureAlgorithm.ES256, SECRET)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }
}

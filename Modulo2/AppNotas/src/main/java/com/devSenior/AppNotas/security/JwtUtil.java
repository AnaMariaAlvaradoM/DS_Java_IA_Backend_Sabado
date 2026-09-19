package com.devSenior.AppNotas.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Extraemos el rol para incluirlo en el token (viene como "ROLE_ADMIN" / "ROLE_USER")
        String rol = userDetails.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER");
        claims.put("rol", rol);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    public boolean esTokenValido(String token, UserDetails userDetails) {
        String username = extraerUsername(token);
        return username.equals(userDetails.getUsername()) && !estaExpirado(token);
    }

    private boolean estaExpirado(String token) {
        return extraerClaims(token).getExpiration().before(new Date());
    }
}

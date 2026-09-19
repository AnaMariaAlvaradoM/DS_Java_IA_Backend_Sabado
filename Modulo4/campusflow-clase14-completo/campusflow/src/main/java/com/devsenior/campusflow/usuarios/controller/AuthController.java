package com.devsenior.campusflow.usuarios.controller;

import com.devsenior.campusflow.usuarios.dto.AuthResponse;
import com.devsenior.campusflow.usuarios.dto.LoginRequest;
import com.devsenior.campusflow.usuarios.dto.RegistroRequest;
import com.devsenior.campusflow.usuarios.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegistroRequest request) {
        return authService.registrar(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

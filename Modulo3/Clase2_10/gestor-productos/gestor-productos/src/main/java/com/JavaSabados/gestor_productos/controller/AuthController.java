package com.JavaSabados.gestor_productos.controller;


import com.JavaSabados.gestor_productos.dto.AuthResponse;
import com.JavaSabados.gestor_productos.dto.LoginRequest;
import com.JavaSabados.gestor_productos.dto.RegistroRequest;
import com.JavaSabados.gestor_productos.service.AuthService;
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
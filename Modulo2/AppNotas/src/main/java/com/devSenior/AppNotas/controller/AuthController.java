package com.devSenior.AppNotas.controller;


import com.devSenior.AppNotas.dto.AuthRequest;
import com.devSenior.AppNotas.dto.AuthResponse;
import com.devSenior.AppNotas.dto.UsuarioDTO;
import com.devSenior.AppNotas.model.Rol;
import com.devSenior.AppNotas.model.Usuario;
import com.devSenior.AppNotas.repository.UsuarioRepository;
import com.devSenior.AppNotas.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<String> registro(@Valid @RequestBody UsuarioDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }
        Usuario nuevo = new Usuario();
        nuevo.setUsername(dto.getUsername());
        nuevo.setPassword(passwordEncoder.encode(dto.getPassword()));
        // Si no envian rol, por defecto es USER
        nuevo.setRol(dto.getRol() != null ? dto.getRol() : Rol.USER);

        usuarioRepository.save(nuevo);
        return ResponseEntity.ok("Usuario registrado correctamente con rol "
                + nuevo.getRol());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UsuarioDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generarToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
package com.devSenior.AppNotas.security;
import com.devSenior.AppNotas.model.Usuario;
import com.devSenior.AppNotas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Convertimos nuestro Rol en una authority con prefijo ROLE_
        // Ej: Rol.ADMIN -> "ROLE_ADMIN"
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));

        // Construimos el UserDetails estandar de Spring Security
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}


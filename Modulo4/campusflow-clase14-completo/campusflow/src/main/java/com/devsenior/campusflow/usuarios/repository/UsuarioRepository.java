package com.devsenior.campusflow.usuarios.repository;

import java.util.Optional;
import com.devsenior.campusflow.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}

package com.devsenior.campusflow.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;
import com.devsenior.campusflow.common.exception.EmailDuplicadoException;
import com.devsenior.campusflow.common.exception.ResourceNotFoundException;
import com.devsenior.campusflow.usuarios.dto.ActualizarUsuarioRequest;
import com.devsenior.campusflow.usuarios.dto.CrearUsuarioRequest;
import com.devsenior.campusflow.usuarios.dto.UsuarioResponse;
import com.devsenior.campusflow.usuarios.mapper.UsuarioMapper;
import com.devsenior.campusflow.usuarios.model.Usuario;
import com.devsenior.campusflow.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse crear(CrearUsuarioRequest request) {
        usuarioRepository.findByEmail(request.getEmail()).ifPresent(usuarioExistente -> {
            throw new EmailDuplicadoException("Ya existe un usuario registrado con ese email");
        });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());

        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(guardado);
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con id " + id));
        return UsuarioMapper.toResponse(usuario);
    }

    public UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con id " + id));

        usuario.setNombre(request.getNombre());
        if (request.getTema() != null) {
            usuario.getPreferencias().setTema(request.getTema());
        }
        if (request.getNotificacionesActivas() != null) {
            usuario.getPreferencias().setNotificacionesActivas(request.getNotificacionesActivas());
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(actualizado);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un usuario con id " + id);
        }
        usuarioRepository.deleteById(id);
    }
}

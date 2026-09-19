package com.devSenior.AppNotas.service;



import com.devSenior.AppNotas.exception.RecursoNoEncontradoException;
import com.devSenior.AppNotas.model.Nota;
import com.devSenior.AppNotas.model.Usuario;
import com.devSenior.AppNotas.repository.NotaRepository;
import com.devSenior.AppNotas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaServiceImpl implements NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Helper: obtiene el id del usuario a partir de su username
    private String obtenerUsuarioId(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username));
        return usuario.getId();
    }

    @Override
    public Nota crear(Nota nota, String username) {
        // La nota siempre queda a nombre de quien la crea
        String usuarioId = obtenerUsuarioId(username);
        nota.setUsuarioId(usuarioId);
        return notaRepository.save(nota);
    }

    @Override
    public List<Nota> listarMisNotas(String username) {
        // Solo las notas cuyo usuarioId coincide con el del usuario
        String usuarioId = obtenerUsuarioId(username);
        return notaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Nota> listarTodas() {
        // Sin filtro: todas las notas del sistema (endpoint solo para ADMIN)
        return notaRepository.findAll();
    }

    @Override
    public Nota obtenerPorId(String id, String username, boolean esAdmin) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Nota no encontrada: " + id));

        String usuarioId = obtenerUsuarioId(username);
        if (!esAdmin && !nota.getUsuarioId().equals(usuarioId)) {
            throw new AccessDeniedException("No puedes ver una nota que no es tuya");
        }
        return nota;
    }

    @Override
    public void eliminar(String id, String username, boolean esAdmin) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Nota no encontrada: " + id));

        String usuarioId = obtenerUsuarioId(username);
        if (!esAdmin && !nota.getUsuarioId().equals(usuarioId)) {
            throw new AccessDeniedException("No puedes eliminar una nota que no es tuya");
        }
        notaRepository.deleteById(id);
    }
}
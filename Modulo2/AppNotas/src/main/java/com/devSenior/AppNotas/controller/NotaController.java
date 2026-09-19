package com.devSenior.AppNotas.controller;

import com.devSenior.AppNotas.dto.NotaDTO;
import com.devSenior.AppNotas.model.Nota;
import com.devSenior.AppNotas.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    // Helper: detecta si quien hace la peticion es ADMIN mirando sus authorities
    private boolean esAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * POST /api/notas — crea una nota. La nota queda a nombre del usuario autenticado.
     * Cualquier usuario autenticado (USER o ADMIN) puede crear.
     */
    @PostMapping
    public ResponseEntity<Nota> crear(@Valid @RequestBody NotaDTO dto, Authentication auth) {
        Nota nota = new Nota();
        nota.setTitulo(dto.getTitulo());
        nota.setContenido(dto.getContenido());
        nota.setEtiquetas(dto.getEtiquetas());

        Nota creada = notaService.crear(nota, auth.getName());
        return ResponseEntity.ok(creada);
    }

    /**
     * GET /api/notas — lista MIS notas (las del usuario autenticado).
     */
    @GetMapping
    public ResponseEntity<List<Nota>> misNotas(Authentication auth) {
        return ResponseEntity.ok(notaService.listarMisNotas(auth.getName()));
    }

    /**
     * GET /api/notas/todas — lista TODAS las notas del sistema.
     * Solo ADMIN: lo garantiza @PreAuthorize a nivel de rol.
     */
    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Nota>> todas() {
        return ResponseEntity.ok(notaService.listarTodas());
    }

    /**
     * GET /api/notas/{id} — obtiene una nota.
     * El servicio valida que sea del usuario o que sea ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Nota> obtener(@PathVariable String id, Authentication auth) {
        Nota nota = notaService.obtenerPorId(id, auth.getName(), esAdmin(auth));
        return ResponseEntity.ok(nota);
    }

    /**
     * DELETE /api/notas/{id} — elimina una nota.
     * El servicio valida que sea del usuario o que sea ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable String id, Authentication auth) {
        notaService.eliminar(id, auth.getName(), esAdmin(auth));
        return ResponseEntity.ok("Nota eliminada");
    }
}
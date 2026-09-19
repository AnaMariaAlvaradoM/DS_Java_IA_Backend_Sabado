package com.devsenior.campusflow.cursos.controller;

import java.util.List;
import com.devsenior.campusflow.cursos.dto.CrearCursoRequest;
import com.devsenior.campusflow.cursos.dto.CursoResponse;
import com.devsenior.campusflow.cursos.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> crear(@Valid @RequestBody CrearCursoRequest request) {
        CursoResponse response = cursoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<CursoResponse> listar() {
        return cursoService.listarCursos();
    }

    @PostMapping("/{cursoId}/estudiantes/{estudianteId}")
    public CursoResponse inscribirEstudiante(@PathVariable Long cursoId, @PathVariable Long estudianteId) {
        return cursoService.inscribirEstudiante(cursoId, estudianteId);
    }
}

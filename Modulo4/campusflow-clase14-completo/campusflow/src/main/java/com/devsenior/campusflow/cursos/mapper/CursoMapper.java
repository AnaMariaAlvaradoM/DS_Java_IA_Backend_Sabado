package com.devsenior.campusflow.cursos.mapper;

import com.devsenior.campusflow.cursos.dto.CursoResponse;
import com.devsenior.campusflow.cursos.model.Curso;

public class CursoMapper {

    public static CursoResponse toResponse(Curso curso) {
        CursoResponse response = new CursoResponse();
        response.setId(curso.getId());
        response.setNombre(curso.getNombre());
        response.setInstructorNombre(
                curso.getInstructor() != null ? curso.getInstructor().getNombre() : null);
        response.setCantidadEstudiantes(curso.getEstudiantes().size());
        return response;
    }
}

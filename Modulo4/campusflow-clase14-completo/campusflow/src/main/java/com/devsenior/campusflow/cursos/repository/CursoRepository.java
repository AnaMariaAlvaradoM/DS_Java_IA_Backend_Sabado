package com.devsenior.campusflow.cursos.repository;

import com.devsenior.campusflow.cursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}

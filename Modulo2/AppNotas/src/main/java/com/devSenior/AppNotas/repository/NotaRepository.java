package com.devSenior.AppNotas.repository;

import com.devSenior.AppNotas.model.Nota;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotaRepository extends MongoRepository<Nota, String> {
    List<Nota> findByUsuarioId(String usuarioId);
}
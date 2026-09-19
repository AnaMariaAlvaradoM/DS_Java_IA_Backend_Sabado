package com.devsenior.campusflow.pagos.repository;


import java.util.Optional;
import com.devsenior.campusflow.pagos.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    Optional<Suscripcion> findByUsuarioId(Long usuarioId);
}
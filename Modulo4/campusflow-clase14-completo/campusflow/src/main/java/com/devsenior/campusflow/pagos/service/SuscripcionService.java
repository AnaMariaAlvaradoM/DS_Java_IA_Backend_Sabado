package com.devsenior.campusflow.pagos.service;

import java.time.LocalDateTime;
import com.devsenior.campusflow.common.exception.ResourceNotFoundException;
import com.devsenior.campusflow.pagos.model.EstadoSuscripcion;
import com.devsenior.campusflow.pagos.model.PlanSuscripcion;
import com.devsenior.campusflow.pagos.model.Suscripcion;
import com.devsenior.campusflow.pagos.repository.SuscripcionRepository;
import com.devsenior.campusflow.usuarios.model.Usuario;
import com.devsenior.campusflow.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository,
                              UsuarioRepository usuarioRepository) {
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void activar(Long usuarioId, PlanSuscripcion plan,
                        String customerId, String subscriptionId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));

        Suscripcion suscripcion = suscripcionRepository
                .findByUsuarioId(usuarioId)
                .orElse(new Suscripcion());

        suscripcion.setUsuario(usuario);
        suscripcion.setPlan(plan);
        suscripcion.setEstado(EstadoSuscripcion.ACTIVA);
        suscripcion.setStripeCustomerId(customerId);
        suscripcion.setStripeSubscriptionId(subscriptionId);
        suscripcion.setFechaInicio(LocalDateTime.now());

        suscripcionRepository.save(suscripcion);
    }
}

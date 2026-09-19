package com.devsenior.campusflow.pagos.controller;

import com.stripe.exception.StripeException;
import com.devsenior.campusflow.common.exception.ResourceNotFoundException;
import com.devsenior.campusflow.pagos.dto.CheckoutResponse;
import com.devsenior.campusflow.pagos.dto.CrearSuscripcionRequest;
import com.devsenior.campusflow.pagos.service.StripeService;
import com.devsenior.campusflow.usuarios.model.Usuario;
import com.devsenior.campusflow.usuarios.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final StripeService stripeService;
    private final UsuarioRepository usuarioRepository;

    public PagoController(StripeService stripeService,
                          UsuarioRepository usuarioRepository) {
        this.stripeService = stripeService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/checkout/pago-unico")
    public CheckoutResponse pagoUnico(Authentication authentication)
            throws StripeException {
        Usuario usuario = usuarioActual(authentication);
        String url = stripeService.crearCheckoutPagoUnico(usuario);
        return new CheckoutResponse(url);
    }

    @PostMapping("/suscripcion")
    public CheckoutResponse suscripcion(
            @Valid @RequestBody CrearSuscripcionRequest request,
            Authentication authentication) throws StripeException {
        Usuario usuario = usuarioActual(authentication);
        String url = stripeService.crearCheckoutSuscripcion(
                usuario, request.getPlan());
        return new CheckoutResponse(url);
    }

    private Usuario usuarioActual(Authentication authentication) {
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));
    }
}
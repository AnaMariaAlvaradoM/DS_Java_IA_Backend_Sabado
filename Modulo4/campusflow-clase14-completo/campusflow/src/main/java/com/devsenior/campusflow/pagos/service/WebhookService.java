package com.devsenior.campusflow.pagos.service;

import java.time.LocalDateTime;
import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.devsenior.campusflow.pagos.model.EventoStripe;
import com.devsenior.campusflow.pagos.repository.EventoStripeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebhookService {

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private final StripeClient stripeClient;
    private final EventoStripeRepository eventoStripeRepository;

    public WebhookService(StripeClient stripeClient,
                          EventoStripeRepository eventoStripeRepository) {
        this.stripeClient = stripeClient;
        this.eventoStripeRepository = eventoStripeRepository;
    }

    @Transactional
    public void procesar(String payload, String firma)
            throws SignatureVerificationException {
        Event event = stripeClient.constructEvent(payload, firma, webhookSecret);

        if (eventoStripeRepository.existsByStripeEventId(event.getId())) {
            return;
        }

        EventoStripe evento = new EventoStripe();
        evento.setStripeEventId(event.getId());
        evento.setTipo(event.getType());
        evento.setRecibidoEn(LocalDateTime.now());
        eventoStripeRepository.save(evento);
    }
}
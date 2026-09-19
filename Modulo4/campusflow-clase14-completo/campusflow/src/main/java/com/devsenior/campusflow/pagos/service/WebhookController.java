package com.devsenior.campusflow.pagos.service;

import com.stripe.exception.SignatureVerificationException;
import com.devsenior.campusflow.pagos.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibir(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String firma) {
        try {
            webhookService.procesar(payload, firma);
            return ResponseEntity.ok("");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Firma inválida");
        }
    }
}
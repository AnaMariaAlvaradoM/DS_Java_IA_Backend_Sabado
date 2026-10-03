package com.devsenior.atiende.ticket;

import com.devsenior.atiende.ticket.dto.AnalisisSentimientoResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class SentimientoController {

    private final SentimientoService sentimientoService;

    public SentimientoController(SentimientoService sentimientoService) {
        this.sentimientoService = sentimientoService;
    }

    @PostMapping("/{id}/sentimiento")
    public AnalisisSentimientoResponse analizar(@PathVariable Long id) {
        return sentimientoService.analizar(id);
    }
}

package com.devsenior.atiende.ticket;

import com.devsenior.atiende.ticket.dto.BorradorResponse;
import com.devsenior.atiende.ticket.dto.ClasificacionResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class CopilotoController {

    private final ClasificacionService clasificacionService;
    private final BorradorService borradorService;

    public CopilotoController(ClasificacionService clasificacionService, BorradorService borradorService) {
        this.clasificacionService = clasificacionService;
        this.borradorService = borradorService;
    }

    @PostMapping("/{id}/clasificacion")
    public ClasificacionResponse clasificar(@PathVariable Long id) {
        return clasificacionService.clasificar(id);
    }

    @PostMapping("/{id}/borrador")
    public BorradorResponse redactarBorrador(@PathVariable Long id) {
        return borradorService.redactar(id);
    }
}
package com.devsenior.atiende.ticket.dto;

import com.devsenior.atiende.ia.dto.FacturaIA;
import com.devsenior.atiende.ticket.Canal;

public record BorradorResponse(
        Long ticketId,
        Canal canal,
        String asunto,
        String cuerpo,
        FacturaIA factura
) {
}
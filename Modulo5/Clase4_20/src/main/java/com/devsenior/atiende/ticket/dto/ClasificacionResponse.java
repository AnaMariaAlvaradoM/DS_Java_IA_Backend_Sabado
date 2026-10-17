package com.devsenior.atiende.ticket.dto;

import com.devsenior.atiende.ia.dto.FacturaIA;

public record ClasificacionResponse(
        TicketResponse ticket,
        FacturaIA factura
) {
}
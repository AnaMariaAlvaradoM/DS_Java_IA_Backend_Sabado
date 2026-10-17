package com.devsenior.atiende.ticket.dto;

import com.devsenior.atiende.ticket.Canal;
import com.devsenior.atiende.ticket.Categoria;
import com.devsenior.atiende.ticket.EstadoTicket;
import com.devsenior.atiende.ticket.Prioridad;
import com.devsenior.atiende.ticket.Sentimiento;
import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String clienteNombre,
        String clienteContacto,
        Canal canal,
        String mensaje,
        EstadoTicket estado,
        Prioridad prioridad,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        Categoria categoria,
        Sentimiento sentimiento,
        String resumen,
        Long numeroPedido
) {
}
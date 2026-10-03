package com.devsenior.atiende.ticket;

import org.springframework.stereotype.Component;

import com.devsenior.atiende.ticket.dto.TicketRespuesta;

@Component
public class TicketMapper {

    public TicketRespuesta aRespuesta(Ticket ticket) {
        // #region agent log
        com.devsenior.atiende.compartido.DebugLog.escribir("H4", "TicketMapper.aRespuesta", "mapeando",
                "{\"id\":" + ticket.getId() + ",\"fechaCreacion\":\"" + ticket.getFechaCreacion()
                        + "\",\"fechaActualizacion\":\"" + ticket.getFechaActualizacion()
                        + "\",\"identidad\":" + System.identityHashCode(ticket) + "}");
        // #endregion
        return new TicketRespuesta(
                ticket.getId(),
                ticket.getClienteNombre(),
                ticket.getClienteContacto(),
                ticket.getCanal(),
                ticket.getMensaje(),
                ticket.getEstado(),
                ticket.getPrioridad(),
                ticket.getFechaCreacion(),
                ticket.getFechaActualizacion());
    }
}

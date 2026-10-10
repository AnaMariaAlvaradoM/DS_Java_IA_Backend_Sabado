package com.devsenior.atiende.ticket;

import com.devsenior.atiende.ia.AsistenteIA;
import com.devsenior.atiende.ia.BibliotecaPrompts;
import com.devsenior.atiende.ia.LectorJsonIA;
import com.devsenior.atiende.ia.RespuestaIA;
import com.devsenior.atiende.ia.RespuestaIAInvalidaException;
import com.devsenior.atiende.ia.dto.FacturaIA;
import com.devsenior.atiende.ticket.dto.ClasificacionResponse;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ClasificacionService {

    private final TicketService ticketService;
    private final AsistenteIA asistenteIA;
    private final BibliotecaPrompts bibliotecaPrompts;
    private final LectorJsonIA lectorJsonIA;

    public ClasificacionService(TicketService ticketService, AsistenteIA asistenteIA,
                                BibliotecaPrompts bibliotecaPrompts, LectorJsonIA lectorJsonIA) {
        this.ticketService = ticketService;
        this.asistenteIA = asistenteIA;
        this.bibliotecaPrompts = bibliotecaPrompts;
        this.lectorJsonIA = lectorJsonIA;
    }

    public ClasificacionResponse clasificar(Long ticketId) {
        TicketResponse ticket = ticketService.buscarPorId(ticketId);
        String instrucciones = bibliotecaPrompts.cargar("clasificacion");
        RespuestaIA respuesta = asistenteIA.completar(instrucciones, ticket.mensaje());

        ClasificacionIA clasificacion = lectorJsonIA.leer(respuesta, ClasificacionIA.class);
        if (!clasificacion.estaCompleta()) {
            throw new RespuestaIAInvalidaException(respuesta.proveedor(), respuesta.texto());
        }
        Long numeroPedido = apareceEnElMensaje(clasificacion.numeroPedido(), ticket.mensaje())
                ? clasificacion.numeroPedido()
                : null;

        TicketResponse clasificado = ticketService.aplicarClasificacion(ticketId, clasificacion, numeroPedido);
        return new ClasificacionResponse(clasificado, FacturaIA.de(respuesta));
    }

    private boolean apareceEnElMensaje(Long numeroPedido, String mensaje) {
        return numeroPedido != null
                && Pattern.compile("\\b" + numeroPedido + "\\b").matcher(mensaje).find();
    }
}
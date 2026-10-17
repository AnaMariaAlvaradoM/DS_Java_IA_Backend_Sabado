package com.devsenior.atiende.ticket;

import com.devsenior.atiende.ia.AsistenteIA;
import com.devsenior.atiende.ia.BibliotecaPrompts;
import com.devsenior.atiende.ia.RespuestaIA;
import com.devsenior.atiende.ia.RespuestaIAInvalidaException;
import com.devsenior.atiende.ticket.dto.AnalisisSentimientoResponse;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import org.springframework.stereotype.Service;

@Service
public class SentimientoService {

    private final TicketService ticketService;
    private final AsistenteIA asistenteIA;
    private final BibliotecaPrompts bibliotecaPrompts;

    public SentimientoService(TicketService ticketService, AsistenteIA asistenteIA,
                              BibliotecaPrompts bibliotecaPrompts) {
        this.ticketService = ticketService;
        this.asistenteIA = asistenteIA;
        this.bibliotecaPrompts = bibliotecaPrompts;
    }

    public AnalisisSentimientoResponse analizar(Long ticketId) {
        TicketResponse ticket = ticketService.buscarPorId(ticketId);
        String instrucciones = bibliotecaPrompts.cargar("sentimiento");
        RespuestaIA respuesta = asistenteIA.completar(instrucciones, ticket.mensaje());

        Sentimiento sentimiento = Sentimiento.desdeTextoIA(respuesta.texto())
                .orElseThrow(() -> new RespuestaIAInvalidaException(
                        respuesta.proveedor(), respuesta.texto()));

        return new AnalisisSentimientoResponse(
                ticketId,
                sentimiento,
                respuesta.proveedor(),
                respuesta.modelo(),
                respuesta.tokensEntrada(),
                respuesta.tokensSalida(),
                respuesta.costoEstimadoUsd(),
                respuesta.milisegundos());
    }
}
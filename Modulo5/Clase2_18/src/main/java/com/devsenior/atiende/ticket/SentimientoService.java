package com.devsenior.atiende.ticket;

import com.devsenior.atiende.ia.AsistenteIA;
import com.devsenior.atiende.ia.RespuestaIA;
import com.devsenior.atiende.ia.RespuestaIAInvalidaException;
import com.devsenior.atiende.ticket.dto.AnalisisSentimientoResponse;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import org.springframework.stereotype.Service;

@Service
public class SentimientoService {

    private static final String INSTRUCCIONES = """
            Eres analista de atención al cliente de una tienda online de café.
            Clasifica el sentimiento del mensaje del cliente:
            - MOLESTO: queja, reclamo, retraso o frustración.
            - NEUTRAL: pregunta o solicitud de información.
            - CONTENTO: agradecimiento o elogio.
            Responde SOLO con una palabra: MOLESTO, NEUTRAL o CONTENTO.
            """;

    private final TicketService ticketService;
    private final AsistenteIA asistenteIA;

    public SentimientoService(TicketService ticketService, AsistenteIA asistenteIA) {
        this.ticketService = ticketService;
        this.asistenteIA = asistenteIA;
    }

    public AnalisisSentimientoResponse analizar(Long ticketId) {
        TicketResponse ticket = ticketService.buscarPorId(ticketId);
        RespuestaIA respuesta = asistenteIA.completar(INSTRUCCIONES, ticket.mensaje());

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

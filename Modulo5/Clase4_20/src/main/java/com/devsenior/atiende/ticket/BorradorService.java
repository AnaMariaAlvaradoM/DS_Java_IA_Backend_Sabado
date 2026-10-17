package com.devsenior.atiende.ticket;

import com.devsenior.atiende.ia.AsistenteIA;
import com.devsenior.atiende.ia.BibliotecaPrompts;
import com.devsenior.atiende.ia.LectorJsonIA;
import com.devsenior.atiende.ia.RespuestaIA;
import com.devsenior.atiende.ia.RespuestaIAInvalidaException;
import com.devsenior.atiende.ia.dto.FacturaIA;
import com.devsenior.atiende.pedido.PedidoService;
import com.devsenior.atiende.ticket.dto.BorradorResponse;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BorradorService {

    record BorradorIA(String asunto, String cuerpo) {
    }

    private final TicketService ticketService;
    private final PedidoService pedidoService;
    private final AsistenteIA asistenteIA;
    private final BibliotecaPrompts bibliotecaPrompts;
    private final LectorJsonIA lectorJsonIA;

    public BorradorService(TicketService ticketService, PedidoService pedidoService,
                           AsistenteIA asistenteIA, BibliotecaPrompts bibliotecaPrompts,
                           LectorJsonIA lectorJsonIA) {
        this.ticketService = ticketService;
        this.pedidoService = pedidoService;
        this.asistenteIA = asistenteIA;
        this.bibliotecaPrompts = bibliotecaPrompts;
        this.lectorJsonIA = lectorJsonIA;
    }

    public BorradorResponse redactar(Long ticketId) {
        TicketResponse ticket = ticketService.buscarPorId(ticketId);
        String instrucciones = bibliotecaPrompts.cargar("borrador-respuesta", Map.of(
                "cliente", ticket.clienteNombre(),
                "formato", formatoPara(ticket.canal()),
                "datos", pedidoService.datosVerificados(ticket.numeroPedido())));
        RespuestaIA respuesta = asistenteIA.completar(instrucciones, ticket.mensaje());

        BorradorIA borrador = lectorJsonIA.leer(respuesta, BorradorIA.class);
        if (borrador.cuerpo() == null || borrador.cuerpo().isBlank()) {
            throw new RespuestaIAInvalidaException(respuesta.proveedor(), respuesta.texto());
        }
        String asunto = ticket.canal() == Canal.CORREO ? borrador.asunto() : null;
        return new BorradorResponse(ticketId, ticket.canal(), asunto, borrador.cuerpo(),
                FacturaIA.de(respuesta));
    }

    private String formatoPara(Canal canal) {
        return switch (canal) {
            case CORREO -> "correo electrónico con asunto, máximo 120 palabras";
            case WHATSAPP, CHAT_WEB -> "mensaje de WhatsApp o chat, máximo 3 frases, asunto vacío";
        };
    }
}
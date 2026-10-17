package com.devsenior.atiende.chat;

import com.devsenior.atiende.chat.dto.ChatRequest;
import com.devsenior.atiende.chat.dto.ChatResponse;
import com.devsenior.atiende.ia.AsistenteIA;
import com.devsenior.atiende.ia.BibliotecaPrompts;
import com.devsenior.atiende.ia.LectorJsonIA;
import com.devsenior.atiende.ia.RespuestaIA;
import com.devsenior.atiende.ia.RespuestaIAInvalidaException;
import com.devsenior.atiende.ia.dto.FacturaIA;
import com.devsenior.atiende.pedido.PedidoService;
import com.devsenior.atiende.ticket.Canal;
import com.devsenior.atiende.ticket.TicketService;
import com.devsenior.atiende.ticket.dto.TicketRequest;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    record RespuestaChatbotIA(String respuesta, boolean escalar) {
    }

    private static final int MAXIMO_MENSAJE_TICKET = 2000;
    private static final List<String> PROMESAS_PROHIBIDAS = List.of("cupón", "cupon", "descuento", "reembols");
    private static final String RESPUESTA_SEGURA =
            "Una persona de nuestro equipo revisará su solicitud y lo contactará pronto.";

    private final PedidoService pedidoService;
    private final TicketService ticketService;
    private final AsistenteIA asistenteIA;
    private final BibliotecaPrompts bibliotecaPrompts;
    private final LectorJsonIA lectorJsonIA;

    public ChatbotService(PedidoService pedidoService, TicketService ticketService,
                          AsistenteIA asistenteIA, BibliotecaPrompts bibliotecaPrompts,
                          LectorJsonIA lectorJsonIA) {
        this.pedidoService = pedidoService;
        this.ticketService = ticketService;
        this.asistenteIA = asistenteIA;
        this.bibliotecaPrompts = bibliotecaPrompts;
        this.lectorJsonIA = lectorJsonIA;
    }

    public ChatResponse responder(ChatRequest request) {
        String instrucciones = bibliotecaPrompts.cargar("chatbot", Map.of(
                "cliente", request.clienteNombre(),
                "datos", pedidoService.datosVerificados(request.numeroPedido())));
        RespuestaIA respuesta = asistenteIA.completar(instrucciones, comoConversacion(request.historial()));

        RespuestaChatbotIA chatbot = lectorJsonIA.leer(respuesta, RespuestaChatbotIA.class);
        if (chatbot.respuesta() == null || chatbot.respuesta().isBlank()) {
            throw new RespuestaIAInvalidaException(respuesta.proveedor(), respuesta.texto());
        }

        if (prometeAlgoNoPermitido(chatbot.respuesta())) {
            return new ChatResponse(RESPUESTA_SEGURA, true, escalarATicket(request), FacturaIA.de(respuesta));
        }
        Long ticketId = chatbot.escalar() ? escalarATicket(request) : null;
        return new ChatResponse(chatbot.respuesta(), chatbot.escalar(), ticketId, FacturaIA.de(respuesta));
    }

    private boolean prometeAlgoNoPermitido(String respuesta) {
        String texto = respuesta.toLowerCase(Locale.ROOT);
        return PROMESAS_PROHIBIDAS.stream().anyMatch(texto::contains);
    }

    private String comoConversacion(List<ChatRequest.Turno> historial) {
        return historial.stream()
                .map(turno -> (turno.rol() == ChatRequest.Rol.CLIENTE ? "Cliente: " : "Grano: ") + turno.texto())
                .collect(Collectors.joining("\n"));
    }

    private Long escalarATicket(ChatRequest request) {
        String mensajesDelCliente = request.historial().stream()
                .filter(turno -> turno.rol() == ChatRequest.Rol.CLIENTE)
                .map(ChatRequest.Turno::texto)
                .collect(Collectors.joining(" | "));
        if (mensajesDelCliente.length() > MAXIMO_MENSAJE_TICKET) {
            mensajesDelCliente = mensajesDelCliente.substring(0, MAXIMO_MENSAJE_TICKET);
        }
        TicketResponse ticket = ticketService.crear(new TicketRequest(
                request.clienteNombre(), request.clienteContacto(), Canal.CHAT_WEB, mensajesDelCliente));
        return ticket.id();
    }
}
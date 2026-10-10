package com.devsenior.atiende.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.devsenior.atiende.ia.AsistenteIA;
import com.devsenior.atiende.ia.RespuestaIA;
import com.devsenior.atiende.ia.RespuestaIAInvalidaException;
import com.devsenior.atiende.ticket.dto.AnalisisSentimientoResponse;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SentimientoServiceTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private AsistenteIA asistenteIA;

    private SentimientoService sentimientoService;

    @BeforeEach
    void setUp() {
        sentimientoService = new SentimientoService(ticketService, asistenteIA);
        when(ticketService.buscarPorId(1L)).thenReturn(ticketDeLaura());
    }

    @Test
    void analizar_respuestaValida_devuelveSentimientoYConsumo() {
        when(asistenteIA.completar(anyString(), anyString()))
                .thenReturn(respuestaIA("MOLESTO"));

        AnalisisSentimientoResponse analisis = sentimientoService.analizar(1L);

        assertEquals(Sentimiento.MOLESTO, analisis.sentimiento());
        assertEquals("openai", analisis.proveedor());
        assertEquals(136, analisis.tokensEntrada());
    }

    @Test
    void analizar_respuestaInvalida_lanzaRespuestaIAInvalida() {
        when(asistenteIA.completar(anyString(), anyString()))
                .thenReturn(respuestaIA("CONTENO"));

        assertThrows(RespuestaIAInvalidaException.class,
                () -> sentimientoService.analizar(1L));
    }

    private TicketResponse ticketDeLaura() {
        return new TicketResponse(1L, "Laura Gómez", "+57 300 123 4567", Canal.WHATSAPP,
                "Pedí el pedido 1045 hace 8 días y no llega", EstadoTicket.ABIERTO,
                Prioridad.MEDIA, LocalDateTime.now(), LocalDateTime.now());
    }

    private RespuestaIA respuestaIA(String texto) {
        return new RespuestaIA(texto, "openai", "gemini-3.5-flash-lite", 136, 5,
                new BigDecimal("0.000053"), 640);
    }
}

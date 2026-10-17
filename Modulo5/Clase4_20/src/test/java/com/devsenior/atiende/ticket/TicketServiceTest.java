package com.devsenior.atiende.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devsenior.atiende.compartido.RecursoNoEncontradoException;
import com.devsenior.atiende.ticket.dto.TicketRequest;
import com.devsenior.atiende.ticket.dto.TicketResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository, new TicketMapper());
    }

    @Test
    void crear_asignaEstadoAbiertoYPrioridadMedia() {
        TicketRequest request = new TicketRequest(
                "Laura Gómez", "+57 300 123 4567", Canal.WHATSAPP,
                "Pedí el pedido 1045 hace 8 días y no llega");
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        TicketResponse respuesta = ticketService.crear(request);

        assertEquals(EstadoTicket.ABIERTO, respuesta.estado());
        assertEquals(Prioridad.MEDIA, respuesta.prioridad());
    }

    @Test
    void buscarPorId_inexistente_lanzaRecursoNoEncontrado() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> ticketService.buscarPorId(99L));
    }

    @Test
    void cambiarEstado_transicionPermitida_actualizaEstado() {
        Ticket ticket = ticketConEstado(EstadoTicket.ABIERTO);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.saveAndFlush(ticket)).thenReturn(ticket);

        TicketResponse respuesta = ticketService.cambiarEstado(1L, EstadoTicket.EN_PROCESO);

        assertEquals(EstadoTicket.EN_PROCESO, respuesta.estado());
    }

    @Test
    void cambiarEstado_transicionBloqueada_lanzaExcepcionYNoGuarda() {
        Ticket ticket = ticketConEstado(EstadoTicket.CERRADO);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(TransicionInvalidaException.class,
                () -> ticketService.cambiarEstado(1L, EstadoTicket.ABIERTO));
        verify(ticketRepository, never()).saveAndFlush(any());
    }

    private Ticket ticketConEstado(EstadoTicket estado) {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setClienteNombre("Laura Gómez");
        ticket.setClienteContacto("+57 300 123 4567");
        ticket.setCanal(Canal.WHATSAPP);
        ticket.setMensaje("Pedí el pedido 1045 hace 8 días y no llega");
        ticket.setEstado(estado);
        ticket.setPrioridad(Prioridad.MEDIA);
        return ticket;
    }
}

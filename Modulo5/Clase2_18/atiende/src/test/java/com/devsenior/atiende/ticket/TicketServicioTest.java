package com.devsenior.atiende.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devsenior.atiende.compartido.RecursoNoEncontradoException;
import com.devsenior.atiende.ticket.dto.CambiarEstadoSolicitud;
import com.devsenior.atiende.ticket.dto.CrearTicketSolicitud;
import com.devsenior.atiende.ticket.dto.TicketRespuesta;

@ExtendWith(MockitoExtension.class)
class TicketServicioTest {

    @Mock
    private TicketRepositorio ticketRepositorio;

    @Mock
    private TicketMapper ticketMapper;

    private TicketServicio ticketServicio;

    @BeforeEach
    void setUp() {
        ticketServicio = new TicketServicio(ticketRepositorio, ticketMapper);
    }

    @Test
    void crear_ticketNuevo_asignaAbiertoYMedia() {
        CrearTicketSolicitud solicitud = new CrearTicketSolicitud(
                "Ana",
                "ana@correo.com",
                CanalTicket.WHATSAPP,
                "Necesito ayuda con mi pedido");

        Ticket ticketGuardado = new Ticket();
        ticketGuardado.setId(1L);
        ticketGuardado.setClienteNombre(solicitud.clienteNombre());
        ticketGuardado.setClienteContacto(solicitud.clienteContacto());
        ticketGuardado.setCanal(solicitud.canal());
        ticketGuardado.setMensaje(solicitud.mensaje());
        ticketGuardado.setEstado(EstadoTicket.ABIERTO);
        ticketGuardado.setPrioridad(PrioridadTicket.MEDIA);
        ticketGuardado.setFechaCreacion(LocalDateTime.now());
        ticketGuardado.setFechaActualizacion(ticketGuardado.getFechaCreacion());

        TicketRespuesta respuestaEsperada = new TicketRespuesta(
                1L,
                solicitud.clienteNombre(),
                solicitud.clienteContacto(),
                solicitud.canal(),
                solicitud.mensaje(),
                EstadoTicket.ABIERTO,
                PrioridadTicket.MEDIA,
                ticketGuardado.getFechaCreacion(),
                ticketGuardado.getFechaActualizacion());

        when(ticketRepositorio.save(any(Ticket.class))).thenReturn(ticketGuardado);
        when(ticketMapper.aRespuesta(ticketGuardado)).thenReturn(respuestaEsperada);

        TicketRespuesta respuesta = ticketServicio.crear(solicitud);

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepositorio).save(captor.capture());

        assertEquals(EstadoTicket.ABIERTO, captor.getValue().getEstado());
        assertEquals(PrioridadTicket.MEDIA, captor.getValue().getPrioridad());
        assertEquals(respuestaEsperada, respuesta);
    }

    @Test
    void buscarPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(ticketRepositorio.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> ticketServicio.obtenerPorId(99L));
    }

    @Test
    void cambiarEstado_transicionPermitida_actualizaEstado() {
        Ticket ticket = new Ticket();
        ticket.setId(10L);
        ticket.setEstado(EstadoTicket.ABIERTO);
        ticket.setFechaCreacion(LocalDateTime.now().minusDays(1));
        ticket.setFechaActualizacion(ticket.getFechaCreacion());

        CambiarEstadoSolicitud solicitud = new CambiarEstadoSolicitud(EstadoTicket.EN_PROCESO);
        TicketRespuesta respuestaEsperada = new TicketRespuesta(
                10L,
                "Ana",
                "ana@correo.com",
                CanalTicket.WHATSAPP,
                "Mensaje",
                EstadoTicket.EN_PROCESO,
                PrioridadTicket.MEDIA,
                ticket.getFechaCreacion(),
                ticket.getFechaActualizacion());

        when(ticketRepositorio.findById(10L)).thenReturn(Optional.of(ticket));
        when(ticketRepositorio.save(ticket)).thenReturn(ticket);
        when(ticketMapper.aRespuesta(ticket)).thenReturn(respuestaEsperada);

        TicketRespuesta respuesta = ticketServicio.cambiarEstado(10L, solicitud);

        assertEquals(EstadoTicket.EN_PROCESO, ticket.getEstado());
        assertEquals(respuestaEsperada, respuesta);
        verify(ticketRepositorio).save(ticket);
        verify(ticketRepositorio, never()).saveAndFlush(any(Ticket.class));
    }

    @Test
    void cambiarEstado_transicionBloqueada_lanzaTransicionEstadoNoPermitidaException() {
        Ticket ticket = new Ticket();
        ticket.setId(10L);
        ticket.setEstado(EstadoTicket.ABIERTO);
        ticket.setFechaCreacion(LocalDateTime.now().minusDays(1));
        ticket.setFechaActualizacion(ticket.getFechaCreacion());

        when(ticketRepositorio.findById(10L)).thenReturn(Optional.of(ticket));

        assertThrows(
                com.devsenior.atiende.ticket.TransicionEstadoNoPermitidaException.class,
                () -> ticketServicio.cambiarEstado(10L, new CambiarEstadoSolicitud(EstadoTicket.RESUELTO)));

        verify(ticketRepositorio, never()).save(any(Ticket.class));
        verify(ticketRepositorio, never()).saveAndFlush(any(Ticket.class));
    }
}

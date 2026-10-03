package com.devsenior.atiende.ticket;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsenior.atiende.compartido.RecursoNoEncontradoException;
import com.devsenior.atiende.ticket.dto.CambiarEstadoSolicitud;
import com.devsenior.atiende.ticket.dto.CrearTicketSolicitud;
import com.devsenior.atiende.ticket.dto.TicketRespuesta;

@Service
public class TicketServicio {

    private final TicketRepositorio ticketRepositorio;
    private final TicketMapper ticketMapper;

    public TicketServicio(TicketRepositorio ticketRepositorio, TicketMapper ticketMapper) {
        this.ticketRepositorio = ticketRepositorio;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public TicketRespuesta crear(CrearTicketSolicitud solicitud) {
        Ticket ticket = new Ticket();
        ticket.setClienteNombre(solicitud.clienteNombre());
        ticket.setClienteContacto(solicitud.clienteContacto());
        ticket.setCanal(solicitud.canal());
        ticket.setMensaje(solicitud.mensaje());
        ticket.setEstado(EstadoTicket.ABIERTO);
        ticket.setPrioridad(PrioridadTicket.MEDIA);
        Ticket guardado = ticketRepositorio.save(ticket);
        return ticketMapper.aRespuesta(guardado);
    }

    @Transactional(readOnly = true)
    public List<TicketRespuesta> listar(EstadoTicket estado) {
        List<Ticket> tickets = estado == null
                ? ticketRepositorio.findAllByOrderByFechaCreacionDesc()
                : ticketRepositorio.findByEstadoOrderByFechaCreacionDesc(estado);
        return tickets.stream().map(ticketMapper::aRespuesta).toList();
    }

    @Transactional(readOnly = true)
    public TicketRespuesta obtenerPorId(Long id) {
        Ticket ticket = buscarOFallar(id);
        // #region agent log
        com.devsenior.atiende.compartido.DebugLog.escribir("H5", "TicketServicio.obtenerPorId", "GET entidad",
                "{\"id\":" + ticket.getId() + ",\"fechaCreacion\":\"" + ticket.getFechaCreacion()
                        + "\",\"fechaActualizacion\":\"" + ticket.getFechaActualizacion()
                        + "\",\"iguales\":" + ticket.getFechaCreacion().equals(ticket.getFechaActualizacion()) + "}");
        // #endregion
        return ticketMapper.aRespuesta(ticket);
    }

    @Transactional
    public TicketRespuesta cambiarEstado(Long id, CambiarEstadoSolicitud solicitud) {
        Ticket ticket = buscarOFallar(id);
        EstadoTicket destino = solicitud.estado();
        if (!ticket.getEstado().puedeCambiarA(destino)) {
            throw new TransicionEstadoNoPermitidaException(ticket.getEstado(), destino);
        }
        ticket.setEstado(destino);
        // #region agent log
        com.devsenior.atiende.compartido.DebugLog.escribir("H1", "TicketServicio.cambiarEstado", "antes de save",
                "{\"id\":" + ticket.getId() + ",\"fechaCreacion\":\"" + ticket.getFechaCreacion()
                        + "\",\"fechaActualizacion\":\"" + ticket.getFechaActualizacion()
                        + "\",\"iguales\":" + ticket.getFechaCreacion().equals(ticket.getFechaActualizacion())
                        + ",\"identidad\":" + System.identityHashCode(ticket) + "}");
        // #endregion
        Ticket actualizado = ticketRepositorio.save(ticket);
        // #region agent log
        com.devsenior.atiende.compartido.DebugLog.escribir("H3", "TicketServicio.cambiarEstado", "despues de save",
                "{\"id\":" + actualizado.getId() + ",\"fechaCreacion\":\"" + actualizado.getFechaCreacion()
                        + "\",\"fechaActualizacion\":\"" + actualizado.getFechaActualizacion()
                        + "\",\"iguales\":" + actualizado.getFechaCreacion().equals(actualizado.getFechaActualizacion())
                        + ",\"mismaInstancia\":" + (ticket == actualizado)
                        + ",\"identidadSave\":" + System.identityHashCode(actualizado) + "}");
        // #endregion
        TicketRespuesta respuesta = ticketMapper.aRespuesta(actualizado);
        // #region agent log
        com.devsenior.atiende.compartido.DebugLog.escribir("H1", "TicketServicio.cambiarEstado", "respuesta mapeada",
                "{\"fechaCreacion\":\"" + respuesta.fechaCreacion()
                        + "\",\"fechaActualizacion\":\"" + respuesta.fechaActualizacion()
                        + "\",\"iguales\":" + respuesta.fechaCreacion().equals(respuesta.fechaActualizacion()) + "}");
        // #endregion
        return respuesta;
    }

    private Ticket buscarOFallar(Long id) {
        return ticketRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el ticket con id " + id));
    }
}

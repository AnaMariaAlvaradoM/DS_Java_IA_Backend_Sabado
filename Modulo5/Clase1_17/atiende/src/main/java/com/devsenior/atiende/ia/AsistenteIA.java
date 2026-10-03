package com.devsenior.atiende.ia;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.devsenior.atiende.compartido.RecursoNoEncontradoException;
import com.devsenior.atiende.ticket.Ticket;
import com.devsenior.atiende.ticket.TicketRepositorio;
import com.devsenior.atiende.ticket.dto.AnalisisSentimientoRespuesta;

@Service
public class AsistenteIA {

    private static final Logger logger = LoggerFactory.getLogger(AsistenteIA.class);

    private final TicketRepositorio ticketRepositorio;
    private final List<ProveedorIA> proveedores;

    public AsistenteIA() {
        this(null, List.of());
    }

    public AsistenteIA(List<ProveedorIA> proveedores) {
        this(null, proveedores);
    }

    public AsistenteIA(TicketRepositorio ticketRepositorio, List<ProveedorIA> proveedores) {
        this.ticketRepositorio = ticketRepositorio;
        this.proveedores = proveedores;
    }

    public AnalisisSentimientoRespuesta analizarPorTicket(Long ticketId) {
        if (ticketRepositorio == null) {
            throw new IllegalStateException("No hay un repositorio de tickets configurado para analizar un ticket.");
        }
        Ticket ticket = ticketRepositorio.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el ticket con id " + ticketId));
        return analizar(ticket.getMensaje(), ticket.getClienteNombre());
    }

    public AnalisisSentimientoRespuesta analizar(String mensaje) {
        return analizar(mensaje, null);
    }

    public AnalisisSentimientoRespuesta analizar(String mensaje, String clienteNombre) {
        if (mensaje == null || mensaje.isBlank()) {
            throw new RespuestaInvalidaIAException("El mensaje del cliente no puede estar vacío.");
        }

        String instrucciones = "Responde solo con uno de estos valores: MOLESTO, NEUTRAL o CONTENTO. "
                + "Analiza el sentimiento del cliente respecto a la atención recibida.";
        String prompt = (clienteNombre == null || clienteNombre.isBlank())
                ? mensaje
                : "Cliente: " + clienteNombre + "\n\n" + mensaje;

        RespuestaInvalidaIAException ultimoErrorValido = null;
        for (ProveedorIA proveedor : proveedores) {
            try {
                RespuestaIA respuesta = proveedor.completar(instrucciones, prompt);
                SentimientoTicket sentimiento = SentimientoTicket.desdeTexto(respuesta.texto());
                return new AnalisisSentimientoRespuesta(
                        sentimiento,
                        respuesta.proveedor(),
                        respuesta.modelo(),
                        respuesta.tokensEntrada(),
                        respuesta.tokensSalida(),
                        respuesta.costoEstimado(),
                        respuesta.milisegundos());
            } catch (RespuestaInvalidaIAException ex) {
                ultimoErrorValido = ex;
                logger.warn("Proveedor IA {} devolvió una respuesta inválida.", proveedor.nombre());
            } catch (RuntimeException ex) {
                logger.warn("Fallo el proveedor IA {}.", proveedor.nombre());
            }
        }

        if (ultimoErrorValido != null) {
            throw ultimoErrorValido;
        }

        throw new ServicioIAIndisponibleException("No se pudo completar la operación con ningún proveedor de IA disponible.");
    }
}

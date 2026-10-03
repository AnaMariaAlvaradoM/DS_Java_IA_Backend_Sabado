package com.devsenior.atiende.ticket.dto;

import java.math.BigDecimal;

import com.devsenior.atiende.ia.SentimientoTicket;

public record AnalisisSentimientoRespuesta(
        SentimientoTicket sentimiento,
        String proveedor,
        String modelo,
        long tokensEntrada,
        long tokensSalida,
        BigDecimal costoEstimado,
        long milisegundos) {
}
